package com.sparta.mypet.domain.auth.dto;

import java.util.List;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.post.dto.PostMappedUserResponseDto;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserWithPostListResponseDto {
	private final Long id;
	private final String nickname;
	private final String email;
	private final List<PostMappedUserResponseDto> freedomList;
	private final List<PostMappedUserResponseDto> boastList;

	@Builder
	public UserWithPostListResponseDto(User user, List<Post> freedomList, List<Post> boastList) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.freedomList = freedomList.stream().map(PostMappedUserResponseDto::new).toList();
		this.boastList = boastList.stream().map(PostMappedUserResponseDto::new).toList();
	}
}
