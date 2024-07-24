package com.sparta.mypet.domain.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.post.dto.PostRequestDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<DataResponseDto<PostResponseDto>> createPost(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody PostRequestDto requestDto, @RequestParam("category") String category) {
		PostResponseDto responseDto = postService.createPost(userDetails.getUser(), requestDto, category);
		return ResponseFactory.created(responseDto, "게시물 생성 성공");
	}

	@PostMapping("/file")
	public ResponseEntity<DataResponseDto<PostResponseDto>> createFilePost(
		@Valid @RequestPart PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestParam("category") String category,
		@RequestPart(value = "file", required = false) List<MultipartFile> files) {

		if (files == null) {
			files = new ArrayList<>();
		}

		if (files.size() > 5) {
			throw new IllegalArgumentException("파일을 5개 이상 업로드할 수 없습니다.");
		}

		PostResponseDto responseDto = postService.createFilePost(userDetails.getUser(), requestDto, category, files);
		return ResponseFactory.created(responseDto, "게시물 생성 성공");
	}

	@PutMapping("/{postId}")
	public ResponseEntity<DataResponseDto<PostResponseDto>> updatePost(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody PostRequestDto requestDto, @PathVariable Long postId) {
		PostResponseDto responseDto = postService.updatePost(userDetails.getUser(), requestDto, postId);
		return ResponseFactory.ok(responseDto, "게시물 수정 성공");
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long postId) {
		postService.deletePost(userDetails.getUser(), postId);
		return ResponseFactory.noContent();
	}

	@GetMapping
	public ResponseEntity<DataResponseDto<Page<PostResponseDto>>> getPosts(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "createdAt, desc") String sortBy) {
		Page<PostResponseDto> responseDtoList = postService.getPosts(page, pageSize, sortBy);
		return ResponseFactory.ok(responseDtoList, "게시물 전체 조회 성공");
	}

	@GetMapping("/{postId}")
	public ResponseEntity<DataResponseDto<PostResponseDto>> getPost(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		User user = (userDetails != null) ? userDetails.getUser() : null;

		PostResponseDto responseDto = postService.getPost(postId, user);
		return ResponseFactory.ok(responseDto, "게시물 단건 조회 성공");
	}
}
