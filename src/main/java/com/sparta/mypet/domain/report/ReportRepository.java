package com.sparta.mypet.domain.report;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.report.entity.Report;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	Optional<Report> findByReportedPost_IdAndReporterUserId(Long reportedPostId, Long reporterUserId);

	@Modifying
	@Query(value = "UPDATE reports r "
		+ "JOIN posts p ON r.reported_post_id = p.post_id "
		+ "SET r.report_status = 'COMPLETED', "
		+ "    r.handle_user_id = :handleUserId "
		+ "WHERE r.report_status IN ('PENDING', 'IN_PROGRESS') "
		+ "  AND p.user_id = :userId",
		nativeQuery = true)
	void markReportsAsCompletedAndSetHandleUser(@Param("userId") Long userId, @Param("handleUserId") Long handleUserId);

}
