package com.sparta.mypet.domain.comment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.comment.dto.CommentResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<DataResponseDto<List<CommentResponseDto>>> getComments(@PathVariable Long postId) {
		List<CommentResponseDto> commentResponseDtoList = commentService.getComments(postId);
		return ResponseFactory.ok(commentResponseDtoList, null);
	}

}
