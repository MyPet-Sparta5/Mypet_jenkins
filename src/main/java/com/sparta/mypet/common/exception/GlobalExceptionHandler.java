package com.sparta.mypet.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;

import jakarta.validation.ConstraintViolationException;

public class GlobalExceptionHandler {

	/**
	 * validation exception handler : valid 에러 메세지 클라이언트에 전달
	 * @param e : valid 에러 캐치
	 * @return 에러 메세지 응답
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageResponseDto> methodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException e) {

		String errorMessages = e.getBindingResult().getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return ResponseFactory.badRequest(errorMessages);
	}

	/**
	 * DB exception handler : DB 저장 에러 메세지 클라이언트에 전달
	 * @param e : DB 저장 에러 캐치
	 * @return 에러 메세지 응답
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<MessageResponseDto> constraintViolationExceptionHandler(ConstraintViolationException e) {

		StringBuilder errorMessages = new StringBuilder();

		e.getConstraintViolations().forEach(violation ->
			errorMessages.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n")
		);

		return ResponseFactory.badRequest(errorMessages.toString());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MessageResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException e) {

		String errorMessage = "Exception caught: " + e.getMessage();

		return ResponseFactory.badRequest(errorMessage);
	}
}
