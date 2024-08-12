package com.sparta.mypet.domain.auth.dto;

import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.Data;

@Data
public class UserSearchCondition {
	private String email;
	private UserStatus status;
	private UserRole role;
}