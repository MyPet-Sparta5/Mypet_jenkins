package com.sparta.mypet.domain.post.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDto {

	private final Long id;
	private final String title;
	private final String content;
	private final Category category;
	private final Long likeCount;
	private final String nickname;
	private final LocalDateTime createAt;
	private final boolean isLike;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.nickname = post.getUser().getNickname();
		this.createAt = post.getCreatedAt();
		this.isLike = false;
	}

	public PostResponseDto(Post post, boolean isLike) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.nickname = post.getUser().getNickname();
		this.createAt = post.getCreatedAt();
		this.isLike = isLike;
	}
}
