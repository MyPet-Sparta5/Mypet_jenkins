package com.sparta.mypet.domain.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidFileException;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.s3.entity.File;
import com.sparta.mypet.domain.s3.entity.FileContentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;

	private static final int TARGET_HEIGHT = 650;

	/**
	 * 파일 업로드 처리
	 *
	 * @param files 업로드할 파일 목록
	 * @param post  파일이 속한 게시물
	 * @return 업로드된 파일 목록
	 */
	@Transactional
	public List<File> uploadFile(List<MultipartFile> files, Post post) {
		List<File> uploadedFiles = new ArrayList<>();

		for (int i = 0; i < files.size(); i++) {
			MultipartFile multiFile = files.get(i);

			FileContentType type = validFile(multiFile);
			String fileName = multiFile.getOriginalFilename();

			try {
				InputStream processedFileStream = processFile(multiFile, type);

				File file = File.builder().post(post).url("").name(fileName).order(i).build();

				File savedFile = fileRepository.save(file);

				String key = savedFile.generateFileKey();

				uploadToS3(processedFileStream, key, multiFile.getContentType());

				String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
				savedFile.updateUrl(fileUrl);

				uploadedFiles.add(savedFile);
			} catch (IOException e) {
				throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage());
			}
		}

		return uploadedFiles;
	}

	public void deleteFiles(List<File> files) {
		for (File file : files) {
			String key = file.generateFileKey();
			amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
		}
	}

	/**
	 * 파일 유효성 검사
	 *
	 * @param file 업로드할 파일
	 */
	private FileContentType validFile(MultipartFile file) {
		if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
			throw new InvalidFileException(GlobalMessage.UPLOAD_FILE_NOT_FOUND.getMessage());
		}

		String fileType = file.getContentType();
		long fileSize = file.getSize();
		FileContentType type = FileContentType.getContentType(fileType);

		if (type == null) {
			throw new InvalidFileException(GlobalMessage.INVALID_TYPE_FILE.getMessage());
		}

		type.validateFileSize(fileSize);
		return type;
	}

	private InputStream processFile(MultipartFile file, FileContentType type) throws IOException {
		BufferedImage processedImage = null;

		switch (type) {
			case JPG, PNG, JPEG:
				processedImage = resizeImage(file);
				break;
			case GIF:
				// GIF는 원본 그대로 반환
				return file.getInputStream();
			case MP4, AVI:
				// 동영상 압축 로직 추가
				return compressVideo(file);
			default:
				throw new InvalidFileException(GlobalMessage.INVALID_TYPE_FILE.getMessage());
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String formatName = type.getType().split("/")[1];

		// 이미지 포맷을 설정하고 저장
		ImageIO.write(processedImage, formatName, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * S3에 파일 업로드
	 *
	 * @param file 파일
	 * @param key  S3에 저장할 파일 키
	 */
	private void uploadToS3(InputStream inputStream, String key, String contentType) {
		ObjectMetadata metadata = new ObjectMetadata();

		try {
			metadata.setContentType(contentType);
			metadata.setContentLength(inputStream.available());

			amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
		} catch (IOException e) {
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage() + e);
		}
	}

	private BufferedImage resizeImage(MultipartFile multipartFile) throws IOException {
		BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());

		if (sourceImage.getHeight() <= TARGET_HEIGHT) {
			return sourceImage;
		}

		double sourceImageRatio = (double)sourceImage.getWidth() / sourceImage.getHeight();

		int newWidth = (int)(TARGET_HEIGHT * sourceImageRatio);

		return Scalr.resize(sourceImage, newWidth, TARGET_HEIGHT);
	}

	private InputStream compressVideo(MultipartFile file) throws IOException {
		// 동영상 압축 로직을 구현합니다
		// 예: FFmpeg 라이브러리 등을 사용할 수 있습니다
		return file.getInputStream(); // 현재는 원본 그대로 반환
	}
}
