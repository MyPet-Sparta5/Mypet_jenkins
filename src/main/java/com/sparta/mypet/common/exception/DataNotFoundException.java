package com.sparta.mypet.common.exception;

import com.sparta.mypet.common.entity.GlobalMessage;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}
}
