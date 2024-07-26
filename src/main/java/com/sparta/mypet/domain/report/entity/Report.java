package com.sparta.mypet.domain.report.entity;

import com.sparta.mypet.common.entity.Timestamped;
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

@Table(name = "reports")
@Entity
@Getter
@NoArgsConstructor
public class Report extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long id;

	@Column(nullable = false)
	private String reportIssue;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportStatus reportStatus;

	@Column
	private Long handleUserId; //신고를 처리한 ADMIN USER

	@Column(nullable = false)
	private Long reporterUserId; //신고를 한 유저

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_user_id")
	private User reportedUser; //신고를 당한 유저

	@Builder
	public Report(User reportedUser, Long reporterUserId, ReportStatus reportStatus, String reportIssue) {
		this.reportedUser = reportedUser;
		this.reporterUserId = reporterUserId;
		this.reportStatus = reportStatus;
		this.reportIssue = reportIssue;
	}

	/**
	 * 신고를 처리할 관리자를 설정합니다.
	 * @param handleUserId 처리할 관리자 ID
	 */
	public void updateHandleUser(Long handleUserId) {
		this.handleUserId = handleUserId;
	}

	/**
	 * 신고 변경 상태를 설정.
	 * @param reportStatus 변경할 상태
	 */
	public void updateReportStatus(ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}
}
