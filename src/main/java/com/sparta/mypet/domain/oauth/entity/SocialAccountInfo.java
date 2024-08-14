package com.sparta.mypet.domain.oauth.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialAccountInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String socialId;
	private SocialType socialType;
	private String targetIdType;
	private String nickname;
	private String email;
	private String accessToken;
	private String refreshToken;

	@Builder
	public SocialAccountInfo(String socialId, String targetIdType, SocialType socialType, String nickname, String email,
		String accessToken, String refreshToken) {
		this.socialId = socialId;
		this.socialType = socialType;
		this.targetIdType = targetIdType;
		this.nickname = nickname;
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
