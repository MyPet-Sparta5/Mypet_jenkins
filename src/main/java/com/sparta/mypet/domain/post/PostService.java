package com.sparta.mypet.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.PostNotFoundException;
import com.sparta.mypet.common.exception.UserMisMatchException;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
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

	@Transactional
	public PostResponseDto createPost(String email, PostRequestDto requestDto, String category) {
		User user = getUserByEmail(email);

		Category postCategory = Category.FREEDOM;

		if (category.equals("BOAST")) {
			postCategory = Category.BOAST;
		}

		Post post = createAndSavePost(user, requestDto.getTitle(), requestDto.getContent(), postCategory);

		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(String email, PostRequestDto requestDto, Long postId) {
		User user = getUserByEmail(email);
		Post post = getPostById(postId);

		checkUser(post, user);

		post.updatePost(requestDto);
		return new PostResponseDto(post);
	}

	@Transactional
	public void deletePost(String email, Long postId) {
		User user = getUserByEmail(email);
		Post post = getPostById(postId);

		checkUser(post, user);

		postRepository.delete(post);
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

	public Page<PostResponseDto> getPosts(int page, int pageSize, String sortBy) {
		// 정렬 기준 - 최신순, 좋아요 수
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		if (sortBy.equals("likes")) {
			sort = Sort.by(Sort.Direction.DESC, "likeCount");
		}
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		Page<Post> postList = postRepository.findAll(pageable);

		return postList.map(PostResponseDto::new);
	}


	private void checkUser(Post post, User user) {
		if (!post.getId().equals(user.getId())) {
			throw new UserMisMatchException(GlobalMessage.NOT_POST_OWNER.getMessage());
		}
	}

	private User getUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}

	public Post getPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

}
