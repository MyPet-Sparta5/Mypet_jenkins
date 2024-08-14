package com.sparta.mypet.domain.suspension.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SuspensionSearchCondition {
	private String email;
	private LocalDate startDate;
	private LocalDate endDate;
}
