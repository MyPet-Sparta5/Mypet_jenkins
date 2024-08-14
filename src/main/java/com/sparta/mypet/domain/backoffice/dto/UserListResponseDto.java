package com.sparta.mypet.domain.backoffice.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.auth.entity.User;

import lombok.Getter;

@Getter
public class UserListResponseDto {

	private final Long id;
	private final String email;
	private final String userRole;
	private final String userStatus;
	private final Integer suspensionCount;
	private final LocalDateTime createdAt;

	public UserListResponseDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.userRole = user.getRole().getAuthority();
		this.userStatus = user.getStatus().toString();
		this.suspensionCount = user.getSuspensionCount();
		this.createdAt = user.getCreatedAt();
	}
}
