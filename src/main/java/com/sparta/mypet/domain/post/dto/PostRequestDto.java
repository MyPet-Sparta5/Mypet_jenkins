package com.sparta.mypet.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

	@NotBlank(message = "제목을 입력해주세요")
	private String title;

	@NotBlank(message = "내용을 입력해주세요")
	private String content;

}
