package com.sparta.mypet.domain.backoffice.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;

import lombok.Getter;

@Getter
public class ReportListResponseDto {
	private Long id;
	private String reportIssue;
	private ReportStatus reportStatus;
	private Long handleUserId; //신고를 처리한 ADMIN USER
	private Long reporterUserId; //신고를 한 유저
	private Long reportedUserId; //신고를 당한 유저 아이디
	private String reportedUserEmail; //신고를 당한 유저 닉네임
	private LocalDateTime createdAt;

	public ReportListResponseDto(Report report) {
		this.id = report.getId();
		this.reportIssue = report.getReportIssue();
		this.reportStatus = report.getReportStatus();
		this.handleUserId = report.getHandleUserId();
		this.reporterUserId = report.getReporterUserId();
		this.reportedUserId = report.getReportedUser().getId();
		this.reportedUserEmail = report.getReportedUser().getEmail();
		this.createdAt = report.getCreatedAt();
	}
}
