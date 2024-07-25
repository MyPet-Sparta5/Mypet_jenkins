package com.sparta.mypet.domain.report;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.report.dto.ReportRequestDto;
import com.sparta.mypet.domain.report.dto.ReportResponseDto;
import com.sparta.mypet.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/reports/users/{userId}")
	public ResponseEntity<DataResponseDto<ReportResponseDto>> createReport(
		@PathVariable(value = "userId") Long reportedUserId,
		@Valid @RequestBody ReportRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		ReportResponseDto responseDto = reportService.createReport(requestDto, reportedUserId,
			userDetails.getUsername());

		return ResponseFactory.created(responseDto, GlobalMessage.CREATE_REPORT_SUCCESS.getMessage());
	}
}
