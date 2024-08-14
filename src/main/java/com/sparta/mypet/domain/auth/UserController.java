package com.sparta.mypet.domain.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.auth.dto.SignupRequestDto;
import com.sparta.mypet.domain.auth.dto.SignupResponseDto;
import com.sparta.mypet.domain.auth.dto.UserPasswordUpdateRequestDto;
import com.sparta.mypet.domain.auth.dto.UserResponseDto;
import com.sparta.mypet.domain.auth.dto.UserUpdateRequestDto;
import com.sparta.mypet.domain.auth.dto.UserUpdateResponseDto;
import com.sparta.mypet.domain.auth.dto.UserWithdrawResponseDto;
import com.sparta.mypet.security.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/users")
	public ResponseEntity<DataResponseDto<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {

		SignupResponseDto responseDto = userService.signup(requestDto);

		return ResponseFactory.created(responseDto, GlobalMessage.CREATE_USER_SUCCESS.getMessage());
	}

	@GetMapping("/users")
	public ResponseEntity<DataResponseDto<UserResponseDto>> getUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UserResponseDto responseDto = userService.getUser(userDetails.getUsername());

		return ResponseFactory.ok(responseDto, GlobalMessage.GET_USER_SUCCESS.getMessage());
	}

	@PutMapping("/users/withdraw")
	public ResponseEntity<DataResponseDto<UserWithdrawResponseDto>> withdrawUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UserWithdrawResponseDto responseDto = userService.withdrawUser(userDetails.getUsername());

		return ResponseFactory.ok(responseDto, GlobalMessage.WITHDRAW_USER_SUCCESS.getMessage());
	}

	@PutMapping("/users")
	public ResponseEntity<DataResponseDto<UserUpdateResponseDto>> updateUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserUpdateRequestDto requestDto) {

		UserUpdateResponseDto responseDto = userService.updateUser(requestDto, userDetails.getUsername());

		return ResponseFactory.ok(responseDto, GlobalMessage.UPDATE_USER_SUCCESS.getMessage());
	}

	@PutMapping("/users/password")
	public ResponseEntity<MessageResponseDto> updateUserPassword(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserPasswordUpdateRequestDto requestDto) {

		userService.updateUserPassword(requestDto, userDetails.getUsername());

		return ResponseFactory.ok(GlobalMessage.UPDATE_USER_PASSWORD_SUCCESS.getMessage());
	}
}
