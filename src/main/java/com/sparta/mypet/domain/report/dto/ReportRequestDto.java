package com.sparta.mypet.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportRequestDto {
	@NotBlank(message = "신고 이유를 작성해야 합니다.")
	private String reportIssue;
}
