package com.sparta.mypet.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequestDto {

	@NotBlank(message = "제목을 입력해주세요")
	private final String title;

	@NotBlank(message = "내용을 입력해주세요")
	private final String content;

	public PostRequestDto(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
