package com.sparta.mypet.domain.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.entity.Category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<DataResponseDto<PostResponseDto>> createPost(@AuthenticationPrincipal UserDetails userDetails,
		@Valid @RequestBody PostRequestDto requestDto, @RequestParam("category") Category category) {
		PostResponseDto responseDto = postService.createPost(userDetails.getUsername(), requestDto, category);
		DataResponseDto<PostResponseDto> response = new DataResponseDto<>(200, "게시물 생성 성공", responseDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
