package com.sparta.mypet.domain.post.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostMappedUserResponseDto {
	private final Long id;
	private final String title;
	private final Category category;
	private final Long likeCount;
	private final LocalDateTime createAt;

	@Builder
	public PostMappedUserResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.createAt = post.getCreatedAt();
	}
}
