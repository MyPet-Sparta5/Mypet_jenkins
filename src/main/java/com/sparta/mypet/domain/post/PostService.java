package com.sparta.mypet.domain.post;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
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
	public PostResponseDto createPost(String email, PostRequestDto requestDto, Category category) {
		User user = getUserByEmail(email);

		Category postCategory = Category.FREEDOM;

		if (category.equals(Category.BOAST)) {
			postCategory = Category.BOAST;
		}

		Post post = createAndSavePost(user, requestDto.getTitle(), requestDto.getContent(), postCategory);

		return new PostResponseDto(post);
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

	private User getUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}
}
