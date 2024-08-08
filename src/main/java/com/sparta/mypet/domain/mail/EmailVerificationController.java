package com.sparta.mypet.domain.mail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.mail.dto.VerificationRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailVerificationController {

	private final EmailVerificationService emailVerificationService;

	@PostMapping("/auth/send-verification")
	public ResponseEntity<MessageResponseDto> sendVerificationCode(@RequestBody VerificationRequest request) {

		boolean result = emailVerificationService.sendVerificationEmail(request.getEmail());
		if (result) {
			return ResponseFactory.ok(null);
		} else {
			return ResponseFactory.tooManyRequests(null);
		}
	}

	@PostMapping("/auth/verify")
	public ResponseEntity<MessageResponseDto> verifyCode(@RequestBody VerificationRequest request) {
		boolean result = emailVerificationService.verifyCode(request.getEmail(), request.getCode());
		if (result) {
			return ResponseFactory.ok(null);
		} else {
			return ResponseFactory.badRequest(null);
		}
	}

}
