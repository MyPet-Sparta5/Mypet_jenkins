package com.sparta.mypet.domain.auth.dto;

import java.util.List;

import com.sparta.mypet.domain.auth.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
	private final Long id;
	private final String nickname;
	private final String email;
	private final List<String> socialLinkedList;

	@Builder
	public UserResponseDto(User user, List<String> socialLinkedList) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.socialLinkedList = socialLinkedList;
	}
}
