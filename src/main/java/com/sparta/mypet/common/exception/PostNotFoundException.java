package com.sparta.mypet.common.exception;

import com.sparta.mypet.common.entity.GlobalMessage;

public class PostNotFoundException extends RuntimeException {
	public PostNotFoundException(String message) {
		super(message);
	}
}
