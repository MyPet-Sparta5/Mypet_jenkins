package com.sparta.mypet.domain.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationResponse {
	private boolean success;
	private Long remainingTime;
}