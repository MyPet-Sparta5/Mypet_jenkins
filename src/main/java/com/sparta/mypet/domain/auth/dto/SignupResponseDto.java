package com.sparta.mypet.domain.auth.dto;

import com.sparta.mypet.domain.auth.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponseDto {
	private final String email;
	private final String nickname;

	@Builder
	public SignupResponseDto(User user) {
		this.email = user.getEmail();
		this.nickname = user.getNickname();
	}
}
