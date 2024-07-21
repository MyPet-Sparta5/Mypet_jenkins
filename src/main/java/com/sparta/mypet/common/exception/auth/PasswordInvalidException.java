package com.sparta.mypet.common.exception.auth;

import com.sparta.mypet.common.entity.GlobalMessage;

public class PasswordInvalidException extends RuntimeException {

	public PasswordInvalidException(String message) {
		super(message);
	}

	public PasswordInvalidException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}