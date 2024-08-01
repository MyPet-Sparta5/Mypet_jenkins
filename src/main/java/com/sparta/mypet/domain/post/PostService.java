package com.sparta.mypet.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidCategoryException;
import com.sparta.mypet.common.exception.custom.InvalidFileException;
import com.sparta.mypet.common.exception.custom.PostNotFoundException;
import com.sparta.mypet.common.exception.custom.UserMisMatchException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;
import com.sparta.mypet.domain.s3.FileService;
import com.sparta.mypet.domain.s3.entity.File;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final FileService fileService;
	private final UserService userService;
	private static final int MAX_FILE_COUNT = 5;

	@Transactional
	public PostResponseDto createPost(String email, PostRequestDto requestDto, String category,
		List<MultipartFile> files) {
		if (files != null && files.size() > MAX_FILE_COUNT) {
			throw new InvalidFileException(GlobalMessage.MAX_FILE_COUNT_EXCEEDED.getMessage());
		}

		User user = userService.findUserByEmail(email);

		Category postCategory = category.equals("BOAST") ? Category.BOAST : Category.FREEDOM;

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
		User user = userService.findUserByEmail(email);

		Post post = findPostById(postId);

		checkPostAuthor(post, user);

		post.updatePost(requestDto);
		return new PostResponseDto(post);
	}

	@Transactional
	public void deletePost(String email, Long postId) {
		User user = userService.findUserByEmail(email);
		Post post = findPostById(postId);

		checkPostAuthor(post, user);

		List<File> files = post.getFiles();
		fileService.deleteFiles(files);

		postRepository.delete(post);
	}

	@Transactional(readOnly = true)
	public Page<PostResponseDto> getPosts(int page, int pageSize, String sortBy, String category, String email,
		String postStatus) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Post> postList;
		Category categoryEnum;
		PostStatus postStatusEnum;

		try { // 포스트 상태 조회
			postStatusEnum = PostStatus.valueOf(postStatus);
		} catch (IllegalArgumentException e) {
			throw new InvalidCategoryException(GlobalMessage.INVALID_ENUM_POST_STATUS.getMessage());
		}

		if (!email.isEmpty()) { // 유저로 필터링할 경우
			userService.findUserByEmail(email);
			postList = postRepository.findByUserName(email, postStatus, pageable);
		} else { // 카테고리로 필터링, 전체조회 할 경우

			try {
				categoryEnum = Category.valueOf(category);
			} catch (IllegalArgumentException e) {
				throw new InvalidCategoryException(GlobalMessage.INVALID_ENUM_CATEGORY.getMessage());
			}

			postList = categoryEnum.equals(Category.DEFAULT) ?
				postRepository.findByPostStatus(postStatusEnum, pageable) :
				postRepository.findByCategoryAndPostStatus(categoryEnum, postStatusEnum, pageable);

		}
		return postList.map(PostResponseDto::new);
	}

	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long postId, String email) {
		Post post = findPostById(postId);

		if (email.isEmpty()) {
			if (post.getPostStatus().equals(PostStatus.ACTIVE)) {
				return new PostResponseDto(post);
			}
			throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
		}

		User user = userService.findUserByEmail(email);

		UserRole role = user.getRole();
		// 게시물의 실제 상태, 권한
		if (post.getPostStatus().equals(PostStatus.INACTIVE) && role.equals(UserRole.USER)) {
			throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
		}

		boolean like = isLikePost(user, postId);
		return new PostResponseDto(post, like);
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

	public void checkPostAuthor(Post post, User user) {
		if (post.getUser().getId().equals(user.getId())) {
			return;
		}

		String role = user.getRole().getAuthority();
		if (UserRole.getAdminAuthority().equals(role) || UserRole.getManagerAuthority().equals(role)) {
			return;
		}

		throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
	}

	public Post findPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	public boolean isLikePost(User user, Long postId) {
		Post post = findPostById(postId);
		return post.isLikedByUser(user);
	}
}
