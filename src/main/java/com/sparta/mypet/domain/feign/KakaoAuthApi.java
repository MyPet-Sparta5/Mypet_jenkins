package com.sparta.mypet.domain.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.mypet.domain.feign.config.FeignConfiguration;

@FeignClient(value = "kakaoAuthApi", url = "https://kauth.kakao.com", configuration = {FeignConfiguration.class})
public interface KakaoAuthApi {

	/**
	 * Access Token을 카카오 서버에 요청
	 * @param clientId REST API Key
	 * @param clientSecret Client Secret Key
	 * @param grantType authorization_code 으로 고정된 값
	 * @param redirectUri 프론트엔드에서 리다이렉트된 URI과 일치해야 됨
	 * @param code 프론트엔드에서 인가된 코드 값
	 * @return <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-response-body">토큰 정보</a>
	 */
	@PostMapping("/oauth/token")
	ResponseEntity<String> getAccessToken(@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret, @RequestParam("grant_type") String grantType,
		@RequestParam("redirect_uri") String redirectUri, @RequestParam("code") String code);
}
