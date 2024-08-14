package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserStatusNotActiveException extends RuntimeException {
	public UserStatusNotActiveException(String message) {
		super(message);
	}

	public UserStatusNotActiveException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
