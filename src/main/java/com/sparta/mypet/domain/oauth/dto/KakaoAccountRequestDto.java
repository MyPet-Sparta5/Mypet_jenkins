package com.sparta.mypet.domain.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoAccountRequestDto {
	private String email;
	private String code;
}
