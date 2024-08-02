package com.sparta.mypet.domain.backoffice.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;

import lombok.Getter;

@Getter
public class ReportListResponseDto {
	private final Long id;
	private final String reportIssue;
	private final ReportStatus reportStatus;
	private final Long handleUserId; //신고를 처리한 ADMIN USER
	private final Long reporterUserId; //신고를 한 유저
	private final Long reportedPostId; //신고를 당한 게시물 ID
	private final Category reportedPostCategory;
	private final LocalDateTime createdAt;

	public ReportListResponseDto(Report report) {
		this.id = report.getId();
		this.reportIssue = report.getReportIssue();
		this.reportStatus = report.getReportStatus();
		this.handleUserId = report.getHandleUserId();
		this.reporterUserId = report.getReporterUserId();
		this.reportedPostId = report.getReportedPost().getId();
		this.reportedPostCategory = report.getReportedPost().getCategory();
		this.createdAt = report.getCreatedAt();
	}
}
