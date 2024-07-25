package com.sparta.mypet.domain.backoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserStatusRequestDto {
	@NotBlank(message = "상태값을 선택해주세요.")
	private String status;
}
