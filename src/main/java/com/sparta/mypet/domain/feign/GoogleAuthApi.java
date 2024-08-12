package com.sparta.mypet.domain.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.mypet.domain.feign.config.FeignConfiguration;

@FeignClient(value = "googleAuthApi", url = "https://oauth2.googleapis.com", configuration = {FeignConfiguration.class})
public interface GoogleAuthApi {
	@PostMapping("/token")
	ResponseEntity<String> getAccessToken(@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret, @RequestParam("grant_type") String grantType,
		@RequestParam("redirect_uri") String redirectUri, @RequestParam("code") String code);

	/**
	 * 유저 Google 연결 끊기 (토큰 해지)
	 * @param token 해지할 액세스 토큰
	 * @return 토큰 해지 결과
	 */
	@PostMapping("/revoke")
	ResponseEntity<String> revokeToken(@RequestParam("token") String token);

	/**
	 * 토큰 정보 확인
	 * @param token 확인할 액세스 토큰
	 * @return 토큰 정보
	 */
	@PostMapping("/tokeninfo")
	ResponseEntity<String> getTokenInfo(@RequestParam("access_token") String token);

	/**
	 * 리프레시 토큰을 사용하여 새 액세스 토큰 발급
	 * @param refreshToken 리프레시 토큰
	 * @param clientId 클라이언트 ID
	 * @param clientSecret 클라이언트 시크릿
	 * @param grantType 권한 부여 유형 (항상 "refresh_token")
	 * @return 새로운 액세스 토큰 정보
	 */
	@PostMapping("/token")
	ResponseEntity<String> refreshAccessToken(@RequestParam("refresh_token") String refreshToken,
		@RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret,
		@RequestParam("grant_type") String grantType);
}
