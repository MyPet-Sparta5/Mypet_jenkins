package com.sparta.mypet.domain.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sparta.mypet.domain.feign.config.FeignConfiguration;

@FeignClient(value = "googleUserApi", url = "https://www.googleapis.com/oauth2", configuration = {
	FeignConfiguration.class})
public interface GoogleUserApi {
	/**
	 * 유저 정보를 Google 서버에 요청
	 * @param header access token을 넣어서 요청
	 * @return 사용자 정보 반환
	 */
	@GetMapping("/v3/userinfo")
	ResponseEntity<String> getUserInfo(@RequestHeader Map<String, String> header);

}
