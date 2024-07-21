package com.sparta.mypet.domain.comment.dto;

import com.sparta.mypet.common.entity.GlobalMessage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
	@NotBlank(message = GlobalMessage.COMMENT_CONTENT_NOT_BLANK_MESSAGE)
	private final String content;

	public CommentRequestDto(String content) {
		this.content = content;
	}
}
