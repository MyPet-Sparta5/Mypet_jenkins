package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserEmailDuplicateException extends RuntimeException {

	public UserEmailDuplicateException(String message) {
		super(message);
	}

	public UserEmailDuplicateException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}