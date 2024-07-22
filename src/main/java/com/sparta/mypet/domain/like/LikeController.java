package com.sparta.mypet.domain.like;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@PostMapping("/posts/{postId}/like")
	public ResponseEntity<MessageResponseDto> likePost(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long postId) {
		likeService.likePost(userDetails.getUsername(), postId);
		return ResponseFactory.ok(null);
	}

}
