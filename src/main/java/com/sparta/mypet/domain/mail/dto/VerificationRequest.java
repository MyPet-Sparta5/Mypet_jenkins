package com.sparta.mypet.domain.mail.dto;

import lombok.Getter;

@Getter
public class VerificationRequest {
	private String email;
	private String code;
}
