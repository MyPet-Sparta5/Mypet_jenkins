package com.sparta.mypet.domain.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.backoffice.dto.ReportListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.ReportStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusResponseDto;
import com.sparta.mypet.domain.report.ReportService;
import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;
import com.sparta.mypet.domain.suspension.SuspensionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackOfficeService {

	private final UserService userService;
	private final ReportService reportService;
	private final SuspensionService suspensionService;

	@Transactional(readOnly = true)
	public Page<UserListResponseDto> getUsers(int page, int pageSize, String sortBy) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<User> userList = userService.findAll(pageable);

		return userList.map(UserListResponseDto::new);
	}

	@Transactional
	public UserStatusResponseDto updateUserStatus(User handleUser, UserStatusRequestDto requestDto, Long userId) {
		User updatedUser = userService.findUserById(userId);
		UserStatus userStatus = UserStatus.valueOf(requestDto.getStatus());

		if (userStatus.equals(UserStatus.SUSPENSION)) {
			suspensionService.processReports(requestDto.getSuspensionIssue(), updatedUser, handleUser);
		}
		updatedUser.updateUserStatus(userStatus);

		return new UserStatusResponseDto(updatedUser);
	}

	@Transactional
	public UserRoleResponseDto updateUserRole(UserRoleRequestDto requestDto, Long userId) {
		User updatedUser = userService.findUserById(userId);
		UserRole userRole = UserRole.valueOf(requestDto.getRole());

		updatedUser.updateUserRole(userRole);
		return new UserRoleResponseDto(updatedUser);
	}

	@Transactional(readOnly = true)
	public Page<ReportListResponseDto> getReports(int page, int pageSize, String sortBy) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Report> reportList = reportService.findAll(pageable);

		return reportList.map(ReportListResponseDto::new);
	}

	@Transactional
	public ReportListResponseDto updateReportStatus(User user, ReportStatusRequestDto requestDto, Long reportId) {
		Report report = reportService.findById(reportId);
		ReportStatus reportStatus = ReportStatus.valueOf(requestDto.getReportStatus());

		report.updateHandleUser(user.getId());
		report.updateReportStatus(reportStatus);

		return new ReportListResponseDto(report);
	}
}
