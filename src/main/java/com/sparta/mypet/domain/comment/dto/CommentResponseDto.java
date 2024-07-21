package com.sparta.mypet.domain.comment.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.comment.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponseDto {
	private final Long id;
	private final String username;
	private final String content;
	private final LocalDateTime createdAt;

	public CommentResponseDto(Comment comment){
		this.id = comment.getId();
		this.username = comment.getUser().getUserName();
		this.content = comment.getContent();
		this.createdAt = comment.getCreatedAt();
	}
}
