package com.sparta.mypet.domain.report.dto;

import java.time.LocalDate;

import com.sparta.mypet.domain.report.entity.ReportStatus;

import lombok.Data;

@Data
public class ReportSearchCondition {
	private String email;
	private ReportStatus status;
	private LocalDate startDate;
	private LocalDate endDate;
}
