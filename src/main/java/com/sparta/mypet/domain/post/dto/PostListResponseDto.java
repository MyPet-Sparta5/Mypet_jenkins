package com.sparta.mypet.domain.post.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;

import lombok.Getter;

@Getter
public class PostListResponseDto {

	private final Long id;
	private final String title;
	private final Category category;
	private final Long likeCount;
	private final String nickname;
	private final PostStatus postStatus;
	private final LocalDateTime createAt;
	private final boolean isLike;

	public PostListResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.nickname = post.getUser().getNickname();
		this.postStatus = post.getPostStatus();
		this.createAt = post.getCreatedAt();
		this.isLike = false;
	}
}
