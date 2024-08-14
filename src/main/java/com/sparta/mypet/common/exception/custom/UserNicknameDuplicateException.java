package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserNicknameDuplicateException extends RuntimeException {
	public UserNicknameDuplicateException(String message) {
		super(message);
	}

	public UserNicknameDuplicateException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
