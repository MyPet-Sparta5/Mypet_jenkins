package com.sparta.mypet.common.exception.custom;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
