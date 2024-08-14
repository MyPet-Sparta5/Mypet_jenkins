package com.sparta.mypet.domain.s3.entity;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidFileException;

public enum FileContentType {
	JPG("image/jpeg"),
	PNG("image/png"),
	JPEG("image/jpeg"),
	MP4("video/mp4"),
	AVI("video/avi"),
	GIF("image/gif");

	private static final long MAX_FILE_SIZE = 20 * 1024 * 1024L; // 20MB
	private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024L; // 100MB

	private final String type;

	FileContentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static FileContentType getContentType(String type) {
		for (FileContentType index : FileContentType.values()) {
			if (index.getType().equals(type)) {
				return index;
			}
		}
		return null;
	}

	public void validateFileSize(long fileSize) {
		switch (this) {
			case JPG, PNG, JPEG, GIF:
				if (fileSize > MAX_FILE_SIZE) {
					throw new InvalidFileException(GlobalMessage.INVALID_SIZE_IMAGE.getMessage());
				}
				break;
			case MP4, AVI:
				if (fileSize > MAX_VIDEO_SIZE) {
					throw new InvalidFileException(GlobalMessage.INVALID_SIZE_VIDEO.getMessage());
				}
				break;
			default:
				throw new InvalidFileException(GlobalMessage.INVALID_TYPE_FILE.getMessage());
		}
	}
}
