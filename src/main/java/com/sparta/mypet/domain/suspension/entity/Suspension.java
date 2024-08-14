package com.sparta.mypet.domain.suspension.entity;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.auth.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Table(name = "suspensions")
@Entity
@Getter
@NoArgsConstructor
public class Suspension {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "suspension_id")
	private Long id;

	@Column
	private Long suspensionHandleUserId;

	@Column(nullable = false)
	private String suspensionIssue;

	@Column(nullable = false)
	private LocalDateTime suspensionStartDatetime;

	@Column(nullable = false)
	private LocalDateTime suspensionEndDatetime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user; //신고를 당한 유저

	@Builder
	public Suspension(Long suspensionHandleUserId, String suspensionIssue, LocalDateTime suspensionStartDatetime,
		LocalDateTime suspensionEndDatetime, User user) {
		this.suspensionHandleUserId = suspensionHandleUserId;
		this.suspensionIssue = suspensionIssue;
		this.suspensionStartDatetime = suspensionStartDatetime;
		this.suspensionEndDatetime = suspensionEndDatetime;
		this.user = user;
	}
}
