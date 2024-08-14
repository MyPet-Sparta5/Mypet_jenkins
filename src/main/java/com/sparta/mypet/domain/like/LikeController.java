package com.sparta.mypet.domain.like;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@PostMapping("/posts/{postId}/likes")
	public ResponseEntity<MessageResponseDto> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long postId) {
		likeService.likePost(userDetails.getUsername(), postId);
		return ResponseFactory.ok(null);
	}

	@DeleteMapping("/posts/{postId}/likes")
	public ResponseEntity<MessageResponseDto> removePostLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long postId) {
		likeService.removePostLike(userDetails.getUsername(), postId);
		return ResponseFactory.ok(null);
	}

}
