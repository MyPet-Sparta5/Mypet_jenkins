package com.sparta.mypet.common.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {

	private int status;
	private String message;

	// 기본 생성자
	public MessageResponseDto() {
	}

	// 매개변수가 있는 생성자
	public MessageResponseDto(int status, String message) {
		this.status = status;
		this.message = message;
	}
}

