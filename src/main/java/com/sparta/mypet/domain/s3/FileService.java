package com.sparta.mypet.domain.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private final ImageCompressionService imageCompressionService;
	private final VideoCompressionService videoCompressionService;

	private static final int MAX_FILE_SIZE_MB = 30; // 동영상 최대 MB

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;

	@Transactional
	public List<UploadedFile> uploadFiles(List<MultipartFile> files, Post post) {
		List<UploadedFile> uploadedFiles = new ArrayList<>();

		for (int i = 0; i < files.size(); i++) {
			MultipartFile multiFile = files.get(i);

			FileContentType type = validateFile(multiFile);
			String fileName = multiFile.getOriginalFilename();

			UploadedFile uploadedFile = UploadedFile.builder()
				.post(post)
				.url("")
				.name(fileName)
				.order(i)
				.build();

			UploadedFile savedFile = processFile(multiFile, type, uploadedFile);
			uploadedFiles.add(savedFile);
		}

		return uploadedFiles;
	}

	public void deleteFiles(List<UploadedFile> files) {
		for (UploadedFile file : files) {
			String key = file.generateFileKey();
			try {
				amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
			} catch (Exception e) {
				log.error("Failed to delete file from S3: {}", key, e);
			}
		}
	}

	private FileContentType validateFile(MultipartFile file) {
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

	private UploadedFile processFile(MultipartFile file, FileContentType type, UploadedFile uploadedFile) {
		InputStream inputStream = null;
		File tempFile = null;

		try {
			switch (type) {
				case JPG, PNG, JPEG:
					inputStream = imageCompressionService.compressImage(file, type);
					break;

				case GIF:
					inputStream = file.getInputStream();
					break;

				case MP4, AVI:
					if (isFileTooLarge(file)) {
						String compressedFilePath = videoCompressionService.compressVideo(file);
						tempFile = new File(compressedFilePath);
						inputStream = new FileInputStream(tempFile);
					} else {
						inputStream = file.getInputStream();
					}
					break;

				default:
					throw new InvalidFileException(GlobalMessage.INVALID_TYPE_FILE.getMessage());
			}

			return uploadToS3(file, uploadedFile, inputStream);

		} catch (IOException e) {
			log.error("Error processing file: {}", file.getOriginalFilename(), e);
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage() + e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.warn("Failed to close InputStream", e);
				}
			}
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
	}

	private boolean isFileTooLarge(MultipartFile file) {
		return file.getSize() > (MAX_FILE_SIZE_MB * 1024 * 1024);
	}

	private UploadedFile uploadToS3(MultipartFile file, UploadedFile uploadedFile, InputStream inputStream) {
		UploadedFile savedFile = fileRepository.save(uploadedFile);
		String key = savedFile.generateFileKey();
		uploadToS3Bucket(inputStream, key, file.getContentType());
		String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
		savedFile.updateUrl(fileUrl);
		return savedFile;
	}

	private void uploadToS3Bucket(InputStream inputStream, String key, String contentType) {
		ObjectMetadata metadata = new ObjectMetadata();
		try {
			metadata.setContentType(contentType);
			metadata.setContentLength(inputStream.available());
			amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
		} catch (IOException e) {
			log.error("Failed to upload file to S3: {}", key, e);
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage() + e);
		}
	}
}
