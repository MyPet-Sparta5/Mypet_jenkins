package com.sparta.mypet.domain.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.comment.dto.CommentPageResponse;
import com.sparta.mypet.domain.comment.dto.CommentRequestDto;
import com.sparta.mypet.domain.comment.dto.CommentResponseDto;
import com.sparta.mypet.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<DataResponseDto<CommentResponseDto>> createComment(
		@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId,
		@Valid @RequestBody CommentRequestDto requestDto) {

		CommentResponseDto responseDto = commentService.createComment(userDetails.getUsername(), postId, requestDto);
		return ResponseFactory.created(responseDto, null);
	}

	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<DataResponseDto<CommentPageResponse>> getComments(@PathVariable Long postId,
		@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt, desc") String sortBy) {

		CommentPageResponse commentPageResponse = commentService.getComments(postId, page, size, sortBy);
		return ResponseFactory.ok(commentPageResponse, null);
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long commentId) {

		commentService.deleteComment(userDetails.getUsername(), commentId);
		return ResponseFactory.noContent();
	}

}
