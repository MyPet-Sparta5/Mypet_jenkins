package com.sparta.mypet.domain.oauth.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialAccountInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long socialId;
	private SocialType socialType;
	private String targetIdType;
	private String nickname;
	private String email;

	@Builder
	public SocialAccountInfo(Long socialId, String targetIdType, SocialType socialType, String nickname, String email) {
		this.socialId = socialId;
		this.socialType = socialType;
		this.targetIdType = targetIdType;
		this.nickname = nickname;
		this.email = email;
	}
}
