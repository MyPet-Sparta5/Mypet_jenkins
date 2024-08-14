package com.sparta.mypet.domain.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialAccountResponse {
	private final String email;
	private final String nickname;

	@Builder
	public SocialAccountResponse(String email, String nickname) {
		this.email = email;
		this.nickname = nickname;
	}
}
