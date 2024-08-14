package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class PostStatusDuplicationException extends RuntimeException {
	public PostStatusDuplicationException(String message) {
		super(message);
	}

	public PostStatusDuplicationException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}