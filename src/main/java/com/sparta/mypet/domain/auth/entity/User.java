package com.sparta.mypet.domain.auth.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sparta.mypet.common.entity.Timestamped;
import com.sparta.mypet.domain.post.entity.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@Column(name = "penalty_end_datetime")
	private LocalDateTime penaltyEndDatetime;

	@Column
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private final List<Post> postList = new ArrayList<>();

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

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateUserStatus(UserStatus status) {
		this.status = status;
	}

	public void updateUserRole(UserRole role) {
		this.role = role;
	}

	public void updatePenaltyEndDatetime(LocalDateTime penaltyEndDatetime) {
		this.penaltyEndDatetime = penaltyEndDatetime;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void addPost(Post post) {
		this.postList.add(post);
	}
}
