package com.sparta.mypet.domain.auth.dto;

import com.sparta.mypet.domain.auth.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
	private final Long id;
	private final String nickname;
	private final String userRole;
	private final String registrationUrl;

	@Builder
	public LoginResponseDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.userRole = user.getRole().getAuthority();
		this.registrationUrl = "";
	}

	public LoginResponseDto(String registrationUrl) {
		this.id = 0L;
		this.nickname = "";
		this.userRole = "";
		this.registrationUrl = registrationUrl;
	}
}
