package com.sparta.mypet.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

	@NotBlank(message = "이름은 공백일 수 없습니다.")
	private String newNickname;

	private String currentPassword;    // 확인용 현재 비밀번호
}
