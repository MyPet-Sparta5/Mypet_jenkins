package com.sparta.mypet.domain.report.entity;

import com.sparta.mypet.common.Timestamped;
import com.sparta.mypet.domain.auth.entity.User;

import jakarta.persistence.*;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_user_id")
	private User reportedUser;

	@Column(nullable = false)
	private Long reporterUserId;

	@Column
	private Long handleUserId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportStatus reportStatus;

	@Column(nullable = false)
	private String reportIssue;

	@Builder
	public Report(User reportedUser, Long reporterUserId, Long handleUserId, ReportStatus reportStatus,
		String reportIssue) {
		this.reportedUser = reportedUser;
		this.reporterUserId = reporterUserId;
		this.handleUserId = handleUserId;
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

}
