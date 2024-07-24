package com.sparta.mypet.domain.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.auth.dto.SignupRequestDto;
import com.sparta.mypet.domain.auth.dto.SignupResponseDto;
import com.sparta.mypet.domain.auth.dto.UserWithPostListResponseDto;
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
	public ResponseEntity<DataResponseDto<UserWithPostListResponseDto>> getUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UserWithPostListResponseDto responseDto = userService.getUser(userDetails.getUsername());

		return ResponseFactory.ok(responseDto, GlobalMessage.GET_USER_SUCCESS.getMessage());
	}

}
