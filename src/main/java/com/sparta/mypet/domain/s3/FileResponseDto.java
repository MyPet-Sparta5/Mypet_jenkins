package com.sparta.mypet.domain.s3;

import com.sparta.mypet.domain.s3.entity.File;

import lombok.Getter;

@Getter
public class FileResponseDto {
	private final String url;
	private final String name;
	private final int order;

	public FileResponseDto(File file) {
		this.url = file.getUrl();
		this.name = file.getName();
		this.order = file.getOrder();
	}
}