package com.sparta.mypet.domain.s3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

	/**
	 * 파일 업로드 처리
	 *
	 * @param files 업로드할 파일 목록
	 * @param post  파일이 속한 게시물
	 * @return 업로드된 파일 목록
	 */
	public List<File> uploadFile(List<MultipartFile> files, Post post) {
		List<File> uploadedFiles = new ArrayList<>();

		int i = 0;

		for (MultipartFile multiFile : files) {
			validFile(multiFile);

			String fileName = multiFile.getOriginalFilename();

			File file = File.builder()
				.post(post)
				.url("")
				.name(fileName)
				.order(i)
				.build();

			file = fileRepository.save(file);

			String key = file.generateFileKey();

			uploadToS3(multiFile, key);

			String fileUrl = generateFileUrl(key);
			file.updateUrl(fileUrl);

			uploadedFiles.add(file);

			i++;
		}

		return uploadedFiles;
	}

	public void deleteFile(List<File> files) {
		for (File file : files) {
			String key = file.generateFileKey();
			amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
		}
	}

	/**
	 * 파일 URL 생성
	 *
	 * @param key S3에 저장된 파일 키
	 * @return 파일의 URL
	 */
	private String generateFileUrl(String key) {
		URL url = amazonS3Client.getUrl(bucket, key);
		return url.toString();
	}

	/**
	 * 파일 유효성 검사
	 *
	 * @param file 업로드할 파일
	 */
	private void validFile(MultipartFile file) {
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
	}

	/**
	 * S3에 파일 업로드
	 *
	 * @param file 파일
	 * @param key  S3에 저장할 파일 키
	 */
	private void uploadToS3(MultipartFile file, String key) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		try (InputStream inputStream = file.getInputStream()) {
			amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
		} catch (IOException e) {
			throw new InvalidFileException(GlobalMessage.UPLOAD_FAIL.getMessage() + e);
		}
	}
}