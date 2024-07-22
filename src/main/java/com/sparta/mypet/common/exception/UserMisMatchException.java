package com.sparta.mypet.common.exception;

import com.sparta.mypet.common.entity.GlobalMessage;

public class UserMisMatchException extends RuntimeException {
	public UserMisMatchException(String message) {
		super(message);
	}
}
