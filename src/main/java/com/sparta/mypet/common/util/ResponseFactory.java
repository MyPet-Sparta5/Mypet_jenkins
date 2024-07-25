package com.sparta.mypet.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;

/**
 * HTTP 응답을 생성하기 위한 클래스입니다.
 * 이 클래스는 다양한 HTTP 상태 코드에 대한 응답을 생성합니다.
 * 필요에 따라 다른 HTTP 상태 코드에 대한 메서드를 추가할 수 있습니다.
 */
public class ResponseFactory {

	private static final int STATUS_OK = HttpStatus.OK.value();
	private static final int STATUS_CREATED = HttpStatus.CREATED.value();
	private static final int STATUS_NO_CONTENT = HttpStatus.NO_CONTENT.value();
	private static final int STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
	private static final int STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
	private static final int STATUS_CONFLICT = HttpStatus.CONFLICT.value();
	private static final int STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
	private static final int STATUS_UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

	// 기본 생성자 : util 클래스이기 때문에 생성되서 사용하지 않도록 private 로 설정
	private ResponseFactory() {
	}

	/**
	 * 주어진 메시지가 유효하지 않은지 확인합니다.
	 *
	 * @param message 확인할 메시지
	 * @return 메시지가 null 이거나 비어있으면 true, 그렇지 않으면 false
	 */
	private static boolean invalidMessage(String message) {
		return message == null || message.isBlank();
	}

	/**
	 * 데이터와 메시지를 포함한 200 OK 응답을 생성합니다.
	 *
	 * @param data    응답에 포함할 데이터
	 * @param message 응답 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static <T> ResponseEntity<DataResponseDto<T>> ok(T data, String message) {
		String okMessage = invalidMessage(message) ? GlobalMessage.MSG_OK.getMessage() : message;
		DataResponseDto<T> responseDto = new DataResponseDto<>(STATUS_OK, okMessage, data);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 메시지만 포함한 200 OK 응답을 생성합니다.
	 *
	 * @param message 응답 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> ok(String message) {
		String okMessage = invalidMessage(message) ? GlobalMessage.MSG_OK.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_OK, okMessage);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 데이터와 메시지를 포함한 201  CREATED 응답을 생성합니다.
	 *
	 * @param data    응답에 포함할 데이터
	 * @param message 응답 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static <T> ResponseEntity<DataResponseDto<T>> created(T data, String message) {
		String okMessage = invalidMessage(message) ? GlobalMessage.MSG_CREATED.getMessage() : message;
		DataResponseDto<T> responseDto = new DataResponseDto<>(STATUS_CREATED, okMessage, data);
		return ResponseEntity.status(STATUS_CREATED).body(responseDto);
	}

	/**
	 * 메시지만 포함한 201 CREATED 응답을 생성합니다.
	 *
	 * @param message 응답 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> created(String message) {
		String okMessage = invalidMessage(message) ? GlobalMessage.MSG_CREATED.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_CREATED, okMessage);
		return ResponseEntity.status(STATUS_CREATED).body(responseDto);
	}

	/**
	 * 204 No Content 응답을 생성합니다.
	 * 이 메서드는 다른 응답 메서드와 달리 본문을 포함하지 않습니다.
	 *
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<Void> noContent() {
		return ResponseEntity.status(STATUS_NO_CONTENT).build();
	}

	// 에러 응답을 위한 메서드들

	/**
	 * 400 Bad Request 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> badRequest(String message) {
		String errorMessage = invalidMessage(message) ? GlobalMessage.MSG_BAD_REQUEST.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_BAD_REQUEST, errorMessage);
		return ResponseEntity.badRequest().body(responseDto);
	}

	/**
	 * 404 Not Found 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> notFound(String message) {
		String errorMessage = invalidMessage(message) ? GlobalMessage.MSG_NOT_FOUND.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_NOT_FOUND, errorMessage);
		return ResponseEntity.status(STATUS_NOT_FOUND).body(responseDto);
	}

	/**
	 * 500 Internal Server Error 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> internalServerError(String message) {
		String errorMessage = invalidMessage(message) ? GlobalMessage.MSG_INTERNAL_SERVER_ERROR.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_INTERNAL_SERVER_ERROR, errorMessage);
		return ResponseEntity.status(STATUS_INTERNAL_SERVER_ERROR).body(responseDto);
	}

	/**
	 * 409 Conflict 응답을 생성합니다.
	 *
	 * @param message 에러 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> conflictError(String message) {
		String errorMessage = invalidMessage(message) ? GlobalMessage.MSG_CONFLICT.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_CONFLICT, errorMessage);
		return ResponseEntity.status(STATUS_CONFLICT).body(responseDto);
	}

	/**
	 * 401 Unauthorized 응답을 생성합니다.
	 * @param message 에러 메시지 (null 이거나 비어있으면 기본 메시지가 사용됨)
	 * @return ResponseEntity 객체
	 */
	public static ResponseEntity<MessageResponseDto> authorizedError(String message) {
		String errorMessage = invalidMessage(message) ? GlobalMessage.MSG_UNAUTHORIZED.getMessage() : message;
		MessageResponseDto responseDto = new MessageResponseDto(STATUS_UNAUTHORIZED, errorMessage);
		return ResponseEntity.status(STATUS_UNAUTHORIZED).body(responseDto);
	}
}
