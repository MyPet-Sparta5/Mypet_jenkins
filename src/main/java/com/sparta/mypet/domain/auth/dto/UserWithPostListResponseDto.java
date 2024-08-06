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
	private final List<PostMappedUserResponseDto> postList;
	private final List<String> socialLinkedList;

	@Builder
	public UserWithPostListResponseDto(User user, List<Post> postList, List<String> socialLinkedList) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.postList = postList.stream().map(PostMappedUserResponseDto::new).toList();
		this.socialLinkedList = socialLinkedList;
	}
}
