package com.sparta.mypet.domain.oauth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.dto.MessageResponseDto;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.auth.dto.LoginResponseDto;
import com.sparta.mypet.domain.oauth.dto.LeaveCallbackRequestDto;
import com.sparta.mypet.domain.oauth.dto.SocialAccountRequestDto;
import com.sparta.mypet.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "OAuthController")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OAuthController {

	private final Map<String, OAuthService> oAuthServices;

	@PostMapping("/oauth/{provider}")
	public ResponseEntity<DataResponseDto<LoginResponseDto>> loginWithSocial(@PathVariable String provider,
		@RequestBody SocialAccountRequestDto requestBody) {

		OAuthService service = getOAuthService(provider);
		LoginResponseDto result = service.processLogin(requestBody.getCode());

		if (result.getRegistrationUrl() == null || result.getRegistrationUrl().isEmpty()) {
			return ResponseFactory.ok(result, GlobalMessage.LOGIN_SUCCESS.getMessage());
		} else {
			return ResponseFactory.found(result, null);
		}
	}

	@DeleteMapping("/oauth/{provider}/leave")
	public ResponseEntity<Void> leaveSocial(@PathVariable String provider,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OAuthService service = getOAuthService(provider);
		service.processLeave(userDetails.getUsername());
		return ResponseFactory.noContent();
	}

	@PostMapping("/oauth/{provider}/leave/callback")
	public ResponseEntity<Void> leaveSocialCallback(@PathVariable String provider,
		@RequestBody LeaveCallbackRequestDto requestDto) {
		OAuthService service = getOAuthService(provider);
		service.processLeaveCallback(requestDto.getUserId());
		return ResponseFactory.noContent();
	}

	@PostMapping("/oauth/{provider}/link")
	public ResponseEntity<MessageResponseDto> linkSocial(@PathVariable String provider,
		@RequestBody SocialAccountRequestDto requestDto) {
		OAuthService service = getOAuthService(provider);
		service.processLink(requestDto.getEmail(), requestDto.getCode());
		return ResponseFactory.ok(null);
	}

	private OAuthService getOAuthService(String provider) {
		return oAuthServices.get(provider + "AccountService");
	}
}
