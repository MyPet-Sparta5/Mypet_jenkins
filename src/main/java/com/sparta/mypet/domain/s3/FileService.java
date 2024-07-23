package com.sparta.mypet.domain.s3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.post.PostRepository;
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

	public File uploadFile(MultipartFile file, Post post, Long userId) {
		try {
			Long postId = post.getId();
			String fileName = file.getOriginalFilename();
			String key = generateFileKey(postId, userId, fileName);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());

			try (InputStream inputStream = file.getInputStream()) {
				amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
			}

			String fileUrl = readFile(postId, userId, fileName);
			File fileEntity = File.builder()
				.post(post)
				.url(fileUrl)
				.name(fileName)
				.build();
			return fileRepository.save(fileEntity);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String readFile(Long postId, Long userId, String fileName) {
		String key = generateFileKey(postId, userId, fileName);
		URL url = amazonS3Client.getUrl(bucket, key);
		return url.toString();
	}

	public void deleteFile(Long postId, Long userId, String fileName) {
		String key = generateFileKey(postId, userId, fileName);
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
	}

	private String generateFileKey(Long postId, Long userId, String fileName) {
		return String.format("%s/%s/%s", userId, postId, fileName);
	}
}