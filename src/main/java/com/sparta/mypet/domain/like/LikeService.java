package com.sparta.mypet.domain.like;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.PostNotFoundException;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.entity.Like;
import com.sparta.mypet.domain.post.PostRepository;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	@Transactional
	public void likePost(String email, Long postId) {
		User user = findUserByEmail(email);

		Post post = findPostById(postId);

		Like like = createAndSaveLike(user, post);
		post.addLike(like);
	}

	private User findUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}

	private Post findPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	private Like createAndSaveLike(User user, Post post) {
		Like like = Like.builder().user(user).post(post).build();
		return likeRepository.save(like);
	}

}
