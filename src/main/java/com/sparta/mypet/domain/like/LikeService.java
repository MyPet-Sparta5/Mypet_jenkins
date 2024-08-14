package com.sparta.mypet.domain.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.entity.Like;
import com.sparta.mypet.domain.post.PostService;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;

	private final UserService userService;

	private final PostService postService;

	@Transactional
	public void likePost(String email, Long postId) {

		User user = userService.findUserByEmail(email);
		Post post = postService.findPostById(postId);

		if (likeRepository.existsByUserAndPost(user, post)) {
			throw new IllegalStateException(GlobalMessage.LIKE_ALREADY_EXISTS.getMessage());
		}

		Like like = createAndSaveLike(user, post);
		post.addLike(like);
	}

	@Transactional
	public void removePostLike(String email, Long postId) {

		User user = userService.findUserByEmail(email);
		Post post = postService.findPostById(postId);

		Like like = likeRepository.findByUserAndPost(user, post)
			.orElseThrow(() -> new IllegalArgumentException(GlobalMessage.LIKE_NOT_FOUND.getMessage()));

		post.removeLike(like);
		likeRepository.delete(like);
	}

	private Like createAndSaveLike(User user, Post post) {
		Like like = Like.builder().user(user).post(post).build();
		return likeRepository.save(like);
	}
}
