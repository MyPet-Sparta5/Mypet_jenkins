package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserInfoDuplicationException extends RuntimeException {
	public UserInfoDuplicationException(String message) {
		super(message);
	}

	public UserInfoDuplicationException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
