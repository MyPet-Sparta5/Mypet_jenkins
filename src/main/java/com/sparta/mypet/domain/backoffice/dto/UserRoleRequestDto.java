package com.sparta.mypet.domain.backoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRoleRequestDto {
	@NotBlank(message = "권한 값을 선택해주세요.")
	private String role;
}