package com.sparta.mypet.domain.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PostStatusDuplicationException;
import com.sparta.mypet.common.exception.custom.ReportDuplicationException;
import com.sparta.mypet.common.exception.custom.UserInfoDuplicationException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.dto.UserSearchCondition;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.backoffice.dto.PostStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.ReportListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.ReportStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.SuspensionListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserRoleResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusResponseDto;
import com.sparta.mypet.domain.post.PostService;
import com.sparta.mypet.domain.post.dto.PostListResponseDto;
import com.sparta.mypet.domain.post.dto.PostResponseDto;
import com.sparta.mypet.domain.post.dto.PostSearchCondition;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;
import com.sparta.mypet.domain.report.ReportService;
import com.sparta.mypet.domain.report.dto.ReportSearchCondition;
import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;
import com.sparta.mypet.domain.suspension.SuspensionService;
import com.sparta.mypet.domain.suspension.dto.SuspensionSearchCondition;
import com.sparta.mypet.domain.suspension.entity.Suspension;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackOfficeService {

	private final UserService userService;
	private final ReportService reportService;
	private final SuspensionService suspensionService;
	private final PostService postService;

	@Transactional(readOnly = true)
	public Page<UserListResponseDto> getUsers(int page, int pageSize, String sortBy, UserSearchCondition condition) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<User> userList = userService.findBySearchCond(condition, pageable);

		return userList.map(UserListResponseDto::new);
	}

	@Transactional
	public UserStatusResponseDto updateUserStatus(User handleUser, UserStatusRequestDto requestDto, Long userId) {
		User updatedUser = userService.findUserById(userId);
		UserStatus userStatus = UserStatus.valueOf(requestDto.getStatus());

		if (userStatus.equals(updatedUser.getStatus())) {
			throw new UserInfoDuplicationException(GlobalMessage.USER_STATUS_DUPLICATE);
		}

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

		if (userRole.equals(updatedUser.getRole())) {
			throw new UserInfoDuplicationException(GlobalMessage.USER_ROLE_DUPLICATE);
		}

		updatedUser.updateUserRole(userRole);
		return new UserRoleResponseDto(updatedUser);
	}

	@Transactional(readOnly = true)
	public Page<ReportListResponseDto> getReports(int page, int pageSize, String sortBy,
		ReportSearchCondition condition) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Report> reportList = reportService.findBySearchCond(condition, pageable);

		return reportList.map(ReportListResponseDto::new);
	}

	@Transactional
	public ReportListResponseDto updateReportStatus(User user, ReportStatusRequestDto requestDto, Long reportId) {
		Report report = reportService.findById(reportId);
		ReportStatus reportStatus = ReportStatus.valueOf(requestDto.getReportStatus());

		if (reportStatus.equals(report.getReportStatus())) {
			throw new ReportDuplicationException(GlobalMessage.REPORT_STATUS_DUPLICATE);
		}
		report.updateHandleUser(user.getId());
		report.updateReportStatus(reportStatus);

		return new ReportListResponseDto(report);
	}

	@Transactional(readOnly = true)
	public Page<SuspensionListResponseDto> getSuspensions(int page, int pageSize, String sortBy,
		SuspensionSearchCondition condition) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Suspension> suspensionList = suspensionService.findBySearchCond(condition, pageable);

		return suspensionList.map(SuspensionListResponseDto::new);
	}

	@Transactional(readOnly = true)
	public Page<PostListResponseDto> getPosts(int page, int pageSize, String sortBy, PostSearchCondition condition) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);
		Page<Post> postList = postService.findBySearchCond(condition, pageable);
		return postList.map(PostListResponseDto::new);
	}

	@Transactional
	public PostResponseDto updatePostStatus(PostStatusRequestDto requestDto, Long postId) {
		Post post = postService.findById(postId);
		PostStatus postStatus = PostStatus.valueOf(requestDto.getPostStatus());

		if (!post.getPostStatus().equals(postStatus)) {
			post.updatePostStatus(postStatus);
		} else {
			throw new PostStatusDuplicationException(GlobalMessage.POST_STATUS_DUPLICATE.getMessage());
		}
		return new PostResponseDto(post);
	}
}
