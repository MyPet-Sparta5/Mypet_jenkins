package com.sparta.mypet.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PostNotFoundException;
import com.sparta.mypet.common.exception.custom.UserMisMatchException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.LikeRepository;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.s3.FileService;
import com.sparta.mypet.domain.s3.entity.File;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;
	private final FileService fileService;

	@Transactional
	public PostResponseDto createPost(String email, PostRequestDto requestDto, String category,
		List<MultipartFile> files) {

		User user = findUserByEmail(email);

		Category postCategory = Category.FREEDOM;

		if (category.equals("BOAST")) {
			postCategory = Category.BOAST;
		}

		Post post = createAndSavePost(user, requestDto.getTitle(), requestDto.getContent(), postCategory);
		user.addPost(post);

		if (files != null && postCategory.equals(Category.BOAST)) {
			List<File> postFiles = fileService.uploadFile(files, post);
			post.addFiles(postFiles);
		}

		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(String email, PostRequestDto requestDto, Long postId) {
		User user = findUserByEmail(email);

		Post post = getPostById(postId);

		checkPostAuthor(post, user);

		post.updatePost(requestDto);
		return new PostResponseDto(post);
	}

	@Transactional
	public void deletePost(String email, Long postId) {
		User user = findUserByEmail(email);
		Post post = getPostById(postId);

		checkPostAuthor(post, user);

		List<File> files = post.getFiles();
		fileService.deleteFile(postId, files);

		postRepository.delete(post);
	}

	@Transactional(readOnly = true)
	public Page<PostResponseDto> getPosts(int page, int pageSize, String sortBy, String category) {

		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Post> postList;

		switch (category) {
			case "BOAST":
				postList = postRepository.findByCategory(Category.BOAST, pageable);
				break;
			case "FREEDOM":
				postList = postRepository.findByCategory(Category.FREEDOM, pageable);
				break;
			case "default":
			default:
				postList = postRepository.findAll(pageable);
				break;
		}

		return postList.map(PostResponseDto::new);
	}

	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long postId, User user) {
		Post post = getPostById(postId);

		boolean isLike = isLikePost(user, post);

		return new PostResponseDto(post, isLike);
	}

	private Post createAndSavePost(User user, String title, String content, Category postCategory) {
		Post post = Post.builder()
			.user(user)
			.postTitle(title)
			.postContent(content)
			.category(postCategory)
			.likeCount(0L)
			.build();
		return postRepository.save(post);
	}

	private void checkPostAuthor(Post post, User user) {
		if (!post.getUser().getId().equals(user.getId())) {
			throw new UserMisMatchException(GlobalMessage.NOT_POST_OWNER.getMessage());
		}
	}

	public Post getPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	public boolean isLikePost(User user, Post post) {
		if (user == null) {
			return false;
		}
		return likeRepository.findByUserAndPost(user, post).isPresent();
	}

	private User findUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}
}
