package com.sparta.mypet.domain.auth.entity;

import lombok.Data;

@Data
public class UserSearchCondition {
	private String email;
	private UserStatus status;
	private UserRole role;
}