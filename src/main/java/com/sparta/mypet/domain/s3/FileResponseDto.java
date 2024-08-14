package com.sparta.mypet.domain.s3;

import com.sparta.mypet.domain.s3.entity.UploadedFile;

import lombok.Getter;

@Getter
public class FileResponseDto {
	private final String url;
	private final String name;
	private final int order;

	public FileResponseDto(UploadedFile uploadedFile) {
		this.url = uploadedFile.getUrl();
		this.name = uploadedFile.getName();
		this.order = uploadedFile.getOrder();
	}
}