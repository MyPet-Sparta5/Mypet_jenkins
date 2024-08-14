package com.sparta.mypet.domain.oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.common.dto.DataResponseDto;
import com.sparta.mypet.common.util.ResponseFactory;
import com.sparta.mypet.domain.oauth.dto.SocialAccountResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SocialAccountController {

	private final SocialAccountService socialAccountService;

	@GetMapping("/users/social-account/infos")
	public ResponseEntity<DataResponseDto<SocialAccountResponse>> getSocialAccountInfo(@RequestParam String key) {
		SocialAccountResponse response = socialAccountService.getSocialAccountInfo(key);
		return ResponseFactory.ok(response, null);
	}

}
