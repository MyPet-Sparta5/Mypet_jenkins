package com.sparta.mypet.domain.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.backoffice.dto.UserListResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BackOfficeController {

	private final BackOfficeService backOfficeService;

	@GetMapping("/user-manage")
	public ResponseEntity<DataResponseDto<Page<UserListResponseDto>>> getUsers(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "createdAt, desc") String sortBy) {
		Page<UserListResponseDto> responseDtoList = backOfficeService.getUsers(page, pageSize, sortBy);
		return ResponseFactory.ok(responseDtoList, "사용자 전체 조회 성공");
	}

}
