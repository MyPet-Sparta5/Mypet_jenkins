package com.sparta.mypet.domain.oauth.dto;

import lombok.Getter;

@Getter
public class SocialAccountRequestDto {
	private String email;
	private String code;
}
