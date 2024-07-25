package com.sparta.mypet.domain.backoffice.dto;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;

import lombok.Getter;

@Getter
public class UserRoleResponseDto {

	private final Long id;
	private final String nickname;
	private final UserRole role;

	public UserRoleResponseDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.role = user.getRole();
	}
}