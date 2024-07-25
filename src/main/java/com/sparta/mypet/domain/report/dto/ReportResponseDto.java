package com.sparta.mypet.domain.report.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.report.entity.Report;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportResponseDto {
	private final Long id;
	private final String reportIssue;
	private final Long reportedUserId;
	private final Long reporterUserId;
	private final LocalDateTime createdAt;

	@Builder
	public ReportResponseDto(Report report) {
		this.id = report.getId();
		this.reportIssue = report.getReportIssue();
		this.reportedUserId = report.getReportedUser().getId();
		this.reporterUserId = report.getReporterUserId();
		this.createdAt = report.getCreatedAt();
	}
}
