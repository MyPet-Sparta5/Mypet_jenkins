package com.sparta.mypet.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoDto {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("has_signed_up")
	private boolean hasSignedUp;

	@JsonProperty("connected_at")
	private String idToken;

	@JsonProperty("synched_at")
	private Integer expiresIn;

	@JsonProperty("properties")
	private String refreshToken;

	@JsonProperty("kakao_account")
	private Integer refreshTokenExpiresIn;

	private String scope;
}
