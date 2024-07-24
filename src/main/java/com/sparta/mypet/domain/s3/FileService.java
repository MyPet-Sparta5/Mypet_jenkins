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
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;

	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
	private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024;

	public List<File> uploadFile(List<MultipartFile> files, Post post, String userEmail) {
		try {
			Long postId = post.getId();
			List<File> uploadedFiles = new ArrayList<>();

			for (int i = 0; i < files.size(); i++) {
				MultipartFile file = files.get(i);
				validFile(file);

				String fileName = file.getOriginalFilename();
				String key = generateFileKey(postId, userEmail, fileName, i);

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(file.getContentType());
				metadata.setContentLength(file.getSize());

				try (InputStream inputStream = file.getInputStream()) {
					amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
				}

				String fileUrl = readFile(postId, userEmail, fileName, i);
				File fileEntity = File.builder()
					.post(post)
					.url(fileUrl)
					.name(fileName)
					.order(i)
					.build();
				uploadedFiles.add(fileRepository.save(fileEntity));
			}

			return uploadedFiles;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String readFile(Long postId, String userEmail, String fileName, int i) {
		String key = generateFileKey(postId, userEmail, fileName, i);
		URL url = amazonS3Client.getUrl(bucket, key);
		return url.toString();
	}

	public void deleteFile(Long postId, String userEmail, List<File> files) {

		int size = files.size();
		for (int i = 0; i < size; i++) {
			String key = generateFileKey(postId, userEmail, files.get(i).getName(), i);
			amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
		}

	}

	private String generateFileKey(Long postId, String userEmail, String fileName, int order) {
		return String.format("%s/%s/%d-%s", userEmail, postId, order, fileName);
	}

	/**
	 * 파일 유효성 검사
	 *
	 * @param file
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

		switch (type) {
			case JPG:
			case PNG:
			case JPEG:
				if (fileSize > MAX_FILE_SIZE) {
					throw new InvalidFileException(GlobalMessage.INVALID_SIZE_IMAGE.getMessage());
				}
				break;
			case MP4:
			case AVI:
				case GIF:
				if (fileSize > MAX_VIDEO_SIZE) {
					throw new InvalidFileException(GlobalMessage.INVALID_SIZE_VIDEO.getMessage());
				}
				break;
			default:
				throw new IllegalArgumentException("");
		}

	}

}