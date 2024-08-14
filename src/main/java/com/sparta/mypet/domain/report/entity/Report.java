package com.sparta.mypet.domain.report.entity;

import com.sparta.mypet.common.entity.Timestamped;
import com.sparta.mypet.domain.post.entity.Post;

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

	@Column(nullable = false)
	private Long reporterUserId; //신고를 한 유저

	@Column
	private Long handleUserId; //신고를 처리한 ADMIN USER, null 허용

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportStatus reportStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_post_id")
	private Post reportedPost; //신고 당한 게시물

	@Builder
	public Report(String reportIssue, Long reporterUserId, ReportStatus reportStatus, Post reportedPost) {
		this.reportIssue = reportIssue;
		this.reporterUserId = reporterUserId;
		this.reportStatus = reportStatus;
		this.reportedPost = reportedPost;
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
