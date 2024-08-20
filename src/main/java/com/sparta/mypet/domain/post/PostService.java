package com.sparta.mypet.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidFileException;
import com.sparta.mypet.common.exception.custom.PostNotFoundException;
import com.sparta.mypet.common.exception.custom.UserMisMatchException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.post.dto.PostListResponseDto;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.dto.PostSearchCondition;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;
import com.sparta.mypet.domain.s3.FileService;
import com.sparta.mypet.domain.s3.entity.UploadedFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private static final int MAX_FILE_COUNT = 5;
	private final PostRepository postRepository;
	private final FileService fileService;
	private final UserService userService;

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
			List<UploadedFile> postUploadedFiles = fileService.uploadFiles(files, post);
			post.addFiles(postUploadedFiles);
		}

		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(String email, PostRequestDto requestDto, Long postId) {
		User user = userService.findUserByEmail(email);

		Post post = findPostById(postId);

		checkUpdateAuthor(post, user);

		post.updatePost(requestDto);
		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto deletePost(String email, Long postId) {
		User user = userService.findUserByEmail(email);
		Post post = findPostById(postId);

		checkDeleteAuthor(post, user);

		post.updatePostStatus(PostStatus.DELETED);

		return new PostResponseDto(post);
	}

	@Transactional(readOnly = true)
	public Page<PostListResponseDto> getPosts(int page, int pageSize, String sortBy, PostSearchCondition condition) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);
		Page<Post> postList = postRepository.findBySearchCond(condition, pageable);
		return postList.map(PostListResponseDto::new);
	}

	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long postId, String email) {
		Post post = findPostById(postId);
		PostStatus postStatus = post.getPostStatus();

		if (email.isEmpty()) {
			if (postStatus.equals(PostStatus.ACTIVE)) {
				return new PostResponseDto(post);
			}
			throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
		}

		User user = userService.findUserByEmail(email);

		UserRole role = user.getRole();

		if ((postStatus.equals(PostStatus.INACTIVE) || postStatus.equals(PostStatus.DELETED))
			&& role.equals(UserRole.USER)) {
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

	public void checkUpdateAuthor(Post post, User user) {
		if (!post.getUser().equals(user)) {
			throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
		}
	}

	public void checkDeleteAuthor(Post post, User user) {
		UserRole role = user.getRole();

		boolean isPostAuthor = post.getUser().equals(user);
		boolean isAdminOrManager = role.equals(UserRole.ADMIN) || role.equals(UserRole.MANAGER);

		if (!isPostAuthor && !isAdminOrManager) {
			throw new UserMisMatchException(GlobalMessage.NOT_AUTHORITY_OWNER.getMessage());
		}
	}

	public Post findPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	public boolean isLikePost(User user, Long postId) {
		Post post = findPostById(postId);
		return post.isLikedByUser(user);
	}

	public void updateReportedPostsStatusByUserId(Long userId, PostStatus status) {
		// 신고 상태가 "REJECTED"인 신고를 제외한 신고된 게시물의 ID를 조회합니다.
		List<Long> postIds = postRepository.findReportedPostIdsByUserId(userId);

		if (!postIds.isEmpty()) {
			// 조회된 게시물의 상태를 업데이트합니다.
			postRepository.updatePostStatus(status, postIds);
		}
	}

	@Transactional(readOnly = true)
	public Post findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	public Page<Post> findBySearchCond(PostSearchCondition condition, Pageable pageable) {
		return postRepository.findBySearchCond(condition, pageable);
	}
}
