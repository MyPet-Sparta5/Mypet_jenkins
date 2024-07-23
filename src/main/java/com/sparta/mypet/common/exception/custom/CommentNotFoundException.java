package com.sparta.mypet.common.exception.custom;

public class CommentNotFoundException extends RuntimeException {
	public CommentNotFoundException(String message) {
		super(message);
	}
}
