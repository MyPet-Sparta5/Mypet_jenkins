package com.sparta.mypet.common.entity;

import lombok.Getter;

@Getter
public enum GlobalMessage {

	// 공통 메시지
	MSG_OK("요청이 성공적으로 완료되었습니다."),
	MSG_CREATED("요청에 대한 데이터가 생성 되었습니다"),
	MSG_BAD_REQUEST("해당 요청을 처리할 수 없습니다."),
	MSG_NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),
	MSG_INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다."),
	MSG_CONFLICT("이미 존재하는 항목입니다."),
	MSG_UNAUTHORIZED("요청에 대해 인증을 실패했습니다."),
	ERROR_MESSAGE_PREFIX("Exception caught: "),

	// 응답 메시지
	// User
	CREATE_USER_SUCCESS("회원가입이 성공적으로 완료되었습니다."),

	// 에러 메세지
	// User
	USER_EMAIL_DUPLICATE("이미 존재하는 이메일입니다."),
	PASSWORD_INVALID("비밀번호가 일치하지 않습니다.");

	private final String message;

	GlobalMessage(String message) {
		this.message = message;
	}
}
