package com.sparta.mypet.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {

	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	@Size(min = 10, max = 50, message = "이메일은 글자 수는 10자에서 50자 사이여야 합니다.")
	@Email(message = "올바른 이메일 형식을 입력하세요.")
	private String email;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 반드시 한 가지 이상의 대문자, 소문자, 숫자, 특수문자를 포함해야합니다.")
	private String password;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자에서 15자 사이여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 반드시 한 가지 이상의 대문자, 소문자, 숫자, 특수문자를 포함해야합니다.")
	private String repeatPassword;

	@NotBlank(message = "이름은 공백일 수 없습니다.")
	private String nickname;
}
