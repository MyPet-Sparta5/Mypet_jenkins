package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserPasswordDuplicationException extends RuntimeException {
	public UserPasswordDuplicationException(String message) {
		super(message);
	}

	public UserPasswordDuplicationException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
