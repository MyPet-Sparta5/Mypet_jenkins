package com.sparta.mypet.common.dto;

import lombok.Getter;

@Getter
public class DataResponseDto<T> extends MessageResponseDto {

	private T data;

	// 기본 생성자
	public DataResponseDto() {
	}

	// 매개변수가 있는 생성자
	public DataResponseDto(int status, String message, T data) {
		super(status, message);
		this.data = data;
	}
}
