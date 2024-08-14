package com.sparta.mypet.domain.backoffice.dto;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.Getter;

@Getter
public class UserStatusResponseDto {
	private final Long id;
	private final String nickname;
	private final UserStatus status;

	public UserStatusResponseDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.status = user.getStatus();
	}
}
