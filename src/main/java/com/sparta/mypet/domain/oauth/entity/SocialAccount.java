package com.sparta.mypet.domain.oauth.entity;

import com.sparta.mypet.domain.auth.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "social_accounts")
@Entity
@Getter
@NoArgsConstructor
public class SocialAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType socialType;

	@Column(name = "social_id", nullable = false)
	private String socialId;

	@Column(nullable = false)
	private String email;

	@Column
	private String accessToken;
	@Column
	private String refreshToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public SocialAccount(SocialType socialType, String socialId, String email) {
		this.socialType = socialType;
		this.socialId = socialId;
		this.email = email;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void updateUser(User user) {
		this.user = user;
	}

}
