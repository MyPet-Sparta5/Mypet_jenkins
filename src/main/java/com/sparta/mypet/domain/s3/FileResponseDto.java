package com.sparta.mypet.domain.s3;

import com.sparta.mypet.domain.s3.File;
import lombok.Getter;

@Getter
public class FileResponseDto {
	private String url;
	private String name;
	private int order;

	public FileResponseDto(File file) {
		this.url = file.getUrl();
		this.name = file.getName();
		this.order = file.getOrder();
	}
}