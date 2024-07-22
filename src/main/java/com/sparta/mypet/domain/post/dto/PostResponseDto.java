package com.sparta.mypet.domain.post.dto;

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

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
	}
}
