package com.sparta.mypet.domain.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.backoffice.dto.ReportListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.ReportStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.SuspensionListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusResponseDto;
import com.sparta.mypet.security.UserDetailsImpl;

import jakarta.validation.Valid;
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

	@PutMapping("/user-manage/{userId}/status")
	public ResponseEntity<DataResponseDto<UserStatusResponseDto>> updateUserStatus(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserStatusRequestDto requestDto, @PathVariable Long userId) {
		UserStatusResponseDto responseDto = backOfficeService.updateUserStatus(userDetails.getUser(), requestDto,
			userId);
		return ResponseFactory.ok(responseDto, "사용자 상태 변경 성공");
	}

	@PutMapping("/user-manage/{userId}/role")
	public ResponseEntity<DataResponseDto<UserRoleResponseDto>> updateUserRole(
		@Valid @RequestBody UserRoleRequestDto requestDto, @PathVariable Long userId) {
		UserRoleResponseDto responseDto = backOfficeService.updateUserRole(requestDto, userId);
		return ResponseFactory.ok(responseDto, "사용자 권한 변경 성공");
	}

	@GetMapping("/report-view")
	public ResponseEntity<DataResponseDto<Page<ReportListResponseDto>>> getReports(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "createdAt, desc") String sortBy) {
		Page<ReportListResponseDto> responseDtoList = backOfficeService.getReports(page, pageSize, sortBy);
		return ResponseFactory.ok(responseDtoList, "신고 목록 전체 조회 성공");
	}

	@PutMapping("/report-view/{reportId}/report-status")
	public ResponseEntity<DataResponseDto<ReportListResponseDto>> updateReportStatus(
		@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ReportStatusRequestDto requestDto,
		@PathVariable Long reportId) {
		ReportListResponseDto responseDto = backOfficeService.updateReportStatus(userDetails.getUser(), requestDto,
			reportId);
		return ResponseFactory.ok(responseDto, "사용자 권한 변경 성공");
	}

	@GetMapping("/suspension-view")
	public ResponseEntity<DataResponseDto<Page<SuspensionListResponseDto>>> getSuspensions(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "suspensionEndDatetime, desc") String sortBy) {
		Page<SuspensionListResponseDto> responseDtoList = backOfficeService.getSuspensions(page, pageSize, sortBy);
		return ResponseFactory.ok(responseDtoList, "신고 목록 전체 조회 성공");
	}
}
