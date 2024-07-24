package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class RefreshTokenInvalidException extends RuntimeException {
	public RefreshTokenInvalidException(String message) {
		super(message);
	}

	public RefreshTokenInvalidException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
