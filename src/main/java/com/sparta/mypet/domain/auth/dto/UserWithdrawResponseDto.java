package com.sparta.mypet.domain.auth.dto;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserWithdrawResponseDto {
	private final String email;
	private final UserStatus status;

	@Builder
	public UserWithdrawResponseDto(User user) {
		this.email = user.getEmail();
		this.status = user.getStatus();
	}
}
