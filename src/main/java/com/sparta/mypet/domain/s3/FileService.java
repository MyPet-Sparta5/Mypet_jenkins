package com.sparta.mypet.domain.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidFileException;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.s3.entity.FileContentType;
import com.sparta.mypet.domain.s3.entity.UploadedFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;

	@Value("${ffmpeg.location}")
	private String ffmpegPath;

	@Value("${ffprobe.location}")
	private String ffprobePath;

	@Value("${temp.file.path}")
	private String tempFilePath;

	private static final int TARGET_HEIGHT = 650;
	private static final long MAX_FILE_SIZE_MB = 30; // 최대 비디오 크기 (MB)
	private static final int VIDEO_BITRATE = 10000000;  // 비트레이트 설정 (대략 10Mbps)

	@Transactional
	public List<UploadedFile> uploadFile(List<MultipartFile> files, Post post) {
		List<UploadedFile> uploadedFiles = new ArrayList<>();

		for (int i = 0; i < files.size(); i++) {
			MultipartFile multiFile = files.get(i);

			FileContentType type = validFile(multiFile);
			String fileName = multiFile.getOriginalFilename();

			try {
				UploadedFile uploadedFile = UploadedFile.builder()
					.post(post)
					.url("")
					.name(fileName)
					.order(i)
					.build();

				UploadedFile savedFile = processFile(multiFile, type, uploadedFile);
				uploadedFiles.add(savedFile);
			} catch (IOException e) {
				throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage() + e);
			}
		}

		return uploadedFiles;
	}

	public void deleteFiles(List<UploadedFile> files) {
		for (UploadedFile file : files) {
			String key = file.generateFileKey();
			amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
		}
	}

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

	private UploadedFile processFile(MultipartFile file, FileContentType type, UploadedFile uploadedFile) throws
		IOException {
		InputStream inputStream;

		switch (type) {
			case JPG, PNG, JPEG:
				inputStream = processImage(file, type);
				return getS3UploadedFile(file, uploadedFile, inputStream);

			case GIF:
				inputStream = file.getInputStream();
				return getS3UploadedFile(file, uploadedFile, inputStream);

			case MP4, AVI:
				File tempFile = null;
				try {
					if (isFileTooLarge(file)) {
						// 파일이 너무 크면 압축을 위해 임시 파일을 저장
						String filePath = saveTempFile(file);
						String compressedFilePath = compressVideo(filePath);
						tempFile = new File(compressedFilePath);
						inputStream = new FileInputStream(tempFile);
					} else {
						// 파일이 크지 않으면 원본 파일을 직접 사용
						inputStream = file.getInputStream();
					}

					return getS3UploadedFile(file, uploadedFile, inputStream);
				} finally {
					// 임시 파일 삭제
					if (tempFile != null && tempFile.exists() && !tempFile.delete()) {
						log.info("");
					}
				}

			default:
				throw new InvalidFileException(GlobalMessage.INVALID_TYPE_FILE.getMessage());
		}
	}

	private boolean isFileTooLarge(MultipartFile file) {
		return file.getSize() > (MAX_FILE_SIZE_MB * 1024 * 1024);
	}

	private UploadedFile getS3UploadedFile(MultipartFile file, UploadedFile uploadedFile, InputStream inputStream) {
		UploadedFile savedFile = fileRepository.save(uploadedFile);
		String key = savedFile.generateFileKey();
		uploadToS3(inputStream, key, file.getContentType());
		String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
		savedFile.updateUrl(fileUrl);
		return savedFile;
	}

	private InputStream processImage(MultipartFile file, FileContentType type) throws IOException {
		BufferedImage processedImage = resizeImage(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String formatName = type.getType().split("/")[1];
		ImageIO.write(processedImage, formatName, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

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

	private String saveTempFile(MultipartFile file) throws IOException {
		String uniqueId = UUID.randomUUID().toString();
		String fileName = file.getOriginalFilename();
		String filePath = tempFilePath + uniqueId + "_" + fileName;

		File tempFile = new File(filePath);
		tempFile.getParentFile().mkdirs();
		file.transferTo(tempFile);

		return filePath;
	}

	private String compressVideo(String filePath) {
		String fileName = new File(filePath).getName();
		String uniqueId = UUID.randomUUID().toString();
		String compressedFilePath = tempFilePath + uniqueId + "_compressed_" + fileName;

		try {
			FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(filePath)
				.addOutput(compressedFilePath)
				.setFormat("mp4")
				.disableSubtitle()
				.setVideoCodec("libx264")
				.setAudioCodec("aac")
				.setVideoBitRate(VIDEO_BITRATE)
				.setVideoFrameRate(30)
				.done();

			FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath), new FFprobe(ffprobePath));
			executor.createJob(builder).run();

			File compressedFile = new File(compressedFilePath);
			if (!compressedFile.exists()) {
				throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage());
			}
		} catch (IOException e) {
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage() + e);
		} finally {
			// 임시 파일 삭제
			new File(filePath).delete();

		}

		return compressedFilePath;
	}
}
