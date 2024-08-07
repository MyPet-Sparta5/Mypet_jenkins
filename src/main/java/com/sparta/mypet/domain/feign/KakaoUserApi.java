package com.sparta.mypet.domain.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.mypet.domain.feign.config.FeignConfiguration;

@FeignClient(value = "kakaoUserApi", url = "https://kapi.kakao.com", configuration = {FeignConfiguration.class})
public interface KakaoUserApi {
	/**
	 * 유저 정보를 카카오 서버에 요청
	 * @param header access token을 넣어서 요청
	 * @return <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response-body">사용자 정보 반환 </a>
	 */
	@GetMapping("/v2/user/me")
	ResponseEntity<String> getUserInfo(@RequestHeader Map<String, String> header);

	/**
	 * 유저 카카오 연결 끊기
	 * @param header access token을 넣어서 요청
	 * @return <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response-body">사용자 정보 반환 </a>
	 */
	@PostMapping("/v1/user/unlink")
	ResponseEntity<String> unlinkUser(@RequestHeader Map<String, String> header,
		@RequestParam("target_id_type") String targetIdType, @RequestParam("target_id") String targetId);
}
