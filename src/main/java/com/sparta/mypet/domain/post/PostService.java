package com.sparta.mypet.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PostNotFoundException;
import com.sparta.mypet.common.exception.custom.UserMisMatchException;
import com.sparta.mypet.common.exception.custom.UserNotFoundException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.LikeRepository;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;

	@Transactional
	public PostResponseDto createPost(User user, PostRequestDto requestDto, String category) {
		userExists(user);

		Category postCategory = Category.FREEDOM;

		if (category.equals("BOAST")) {
			postCategory = Category.BOAST;
		}

		Post post = createAndSavePost(user, requestDto.getTitle(), requestDto.getContent(), postCategory);

		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(User user, PostRequestDto requestDto, Long postId) {
		userExists(user);
		Post post = getPostById(postId);

		checkPostAuthor(post, user);

		post.updatePost(requestDto);
		return new PostResponseDto(post);
	}

	@Transactional
	public void deletePost(User user, Long postId) {
		userExists(user);
		Post post = getPostById(postId);

		checkPostAuthor(post, user);

		postRepository.delete(post);
	}

	@Transactional(readOnly = true)
	public Page<PostResponseDto> getPosts(int page, int pageSize, String sortBy) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Post> postList = postRepository.findAll(pageable);

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

	public void userExists(User user) { // request to post service
		userRepository.findById(user.getId())
			.orElseThrow(() -> new UserNotFoundException(GlobalMessage.USER_NOT_FOUND.getMessage()));
	}

	public Post getPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	public boolean isLikePost(User user, Post post) {
		if(user == null){
			return false;
		}
		return likeRepository.findByUserAndPost(user, post).isPresent();
	}
}
