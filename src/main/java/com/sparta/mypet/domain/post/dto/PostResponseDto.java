package com.sparta.mypet.domain.post.dto;

import com.sparta.mypet.domain.post.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

	private Long id;
	private String title;
	private String content;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
	}
}
