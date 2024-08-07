package com.sparta.mypet.domain.oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.auth.dto.LoginResponseDto;
import com.sparta.mypet.domain.oauth.dto.KakaoAccountRequestDto;
import com.sparta.mypet.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Kakao API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoAccountController {

	private final KakaoAccountService kakaoAccountService;

	@PostMapping("/oauth/kakao")
	public ResponseEntity<DataResponseDto<LoginResponseDto>> loginWithKakao(
		@RequestBody KakaoAccountRequestDto requestBody) {
		log.info("code = {}", requestBody.getCode());
		LoginResponseDto result = kakaoAccountService.processKakaoLogin(requestBody.getCode());

		if (result.getRegistrationUrl() == null || result.getRegistrationUrl().isEmpty()) {
			return ResponseFactory.ok(result, GlobalMessage.LOGIN_SUCCESS.getMessage());
		} else {
			return ResponseFactory.found(result, null);
		}
	}

	@DeleteMapping("/oauth/kakao/leave")
	public ResponseEntity<Void> leaveKakao(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		kakaoAccountService.processKakaoLeave(userDetails.getUsername());
		return ResponseFactory.noContent();
	}

	@PostMapping("/oauth/kakao/link")
	public ResponseEntity<MessageResponseDto> linkKakao(@RequestBody KakaoAccountRequestDto requestDto) {
		kakaoAccountService.processKakaoLink(requestDto.getEmail(), requestDto.getCode());
		return ResponseFactory.ok(null);
	}

}
