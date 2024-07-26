package com.sparta.mypet.domain.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.ReportNotFoundException;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.report.dto.ReportRequestDto;
import com.sparta.mypet.domain.report.dto.ReportResponseDto;
import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportRepository reportRepository;
	private final UserService userService;

	public ReportResponseDto createReport(ReportRequestDto requestDto, Long reportedUserId, String reporterUsername) {

		User reportedUser = userService.findUserById(reportedUserId);
		User reporterUser = userService.findUserByEmail(reporterUsername);

		if (reporterUser.getId().equals(reportedUserId)) {
			throw new IllegalArgumentException(GlobalMessage.SELF_REPORT_NOT.getMessage());
		}

		Report report = Report.builder()
			.reportedUser(reportedUser)
			.reporterUserId(reporterUser.getId())
			.reportStatus(ReportStatus.PENDING)
			.reportIssue(requestDto.getReportIssue())
			.build();

		Report saveReport = reportRepository.save(report);

		return ReportResponseDto.builder()
			.report(saveReport)
			.build();
	}

	@Transactional(readOnly = true)
	public Page<Report> findAll(Pageable pageable) {
		return reportRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Report findById(Long id) {
		return reportRepository.findById(id).orElseThrow(() ->
			new ReportNotFoundException(GlobalMessage.REPORT_NOT_FOUND.getMessage()));
	}
}
