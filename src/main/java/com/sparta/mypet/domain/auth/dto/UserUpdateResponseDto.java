package com.sparta.mypet.domain.auth.dto;

import com.sparta.mypet.domain.auth.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateResponseDto {
	private final String newNickname;

	@Builder
	public UserUpdateResponseDto(User user) {
		this.newNickname = user.getNickname();
	}
}
