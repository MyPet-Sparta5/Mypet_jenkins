package com.sparta.mypet.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {

	private String currentPassword;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 반드시 한 가지 이상의 대문자, 소문자, 숫자, 특수문자를 포함해야합니다.")
	private String newPassword;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 반드시 한 가지 이상의 대문자, 소문자, 숫자, 특수문자를 포함해야합니다.")
	private String newRepeatPassword;

}
