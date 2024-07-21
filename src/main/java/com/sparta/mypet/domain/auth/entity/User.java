package com.sparta.mypet.domain.auth.entity;

import com.sparta.mypet.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Column(name = "penalty_count", nullable = false)
	private Integer penaltyCount;

	@Column
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	@Builder
	public User(String email, String password, String nickname, Integer penaltyCount, UserRole role,
		UserStatus status) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.penaltyCount = penaltyCount;
		this.role = role;
		this.status = status;
	}
}
