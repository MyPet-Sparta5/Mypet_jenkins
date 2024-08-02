package com.sparta.mypet.domain.suspension;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.post.PostService;
import com.sparta.mypet.domain.post.entity.PostStatus;
import com.sparta.mypet.domain.report.ReportService;
import com.sparta.mypet.domain.suspension.entity.Suspension;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuspensionService {

	private final ReportService reportService;
	private final PostService postService;
	private final SuspensionRepository suspensionRepository;

	@Transactional
	public void processReports(String suspensionIssue, User user, User handleUser) {
		int suspensionCount = user.getSuspensionCount();

		suspendUser(user, suspensionCount, suspensionIssue, handleUser);
	}

	private void suspendUser(User user, int currentSuspensionCount, String suspensionIssue, User handleUser) {
		int newSuspensionCount = currentSuspensionCount + 1;

		int suspensionDays = getSuspensionDays(newSuspensionCount);
		LocalDateTime suspensionEndDatetime = LocalDateTime.now().plusDays(suspensionDays);

		//유저 상태 변경 및 count 변경
		user.updateSuspendUser(newSuspensionCount);
		//해당 user의 Report 상태가 Pending이나 in_progress인 경우 completed로 바꾸고, handleuser는 status 변경하는 관리자로 지정
		reportService.markReportsAsCompletedAndSetHandleUser(user.getId(), handleUser.getId());
		//report 반려상태가 아닌 post의 status를 inactive 상태로 변경
		postService.updateReportedPostsStatusByUserId(user.getId(), PostStatus.INACTIVE);

		Suspension suspension = Suspension.builder().
			suspensionHandleUserId(handleUser.getId()).
			suspensionIssue(suspensionIssue).
			user(user).
			suspensionStartDatetime(LocalDateTime.now()).
			suspensionEndDatetime(suspensionEndDatetime).build();

		suspensionRepository.save(suspension);
	}

	private int getSuspensionDays(int suspensionCount) {
		return switch (suspensionCount) {
			case 1 -> 3;
			case 2 -> 7;
			case 3 -> 30;
			case 4 -> 36500; //약 100년으로 영구정지화 시킴.
			default -> throw new IllegalArgumentException("Invalid penalty count");
		};
	}

	public Page<Suspension> findAll(Pageable pageable) {
		return suspensionRepository.findAll(pageable);
	}
}
