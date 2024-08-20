package com.sparta.mypet.domain.report;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.report.dto.ReportSearchCondition;
import com.sparta.mypet.domain.report.entity.Report;

public interface ReportRepositoryQuery {
	Page<Report> findBySearchCond(ReportSearchCondition condition, Pageable pageable);

	List<Long> findReportIdsByPostUserId(Long userId);

	void updateReportStatusAndHandleUserIdByReportIds(Long handleUserId, List<Long> reportIds);
}
