package com.sparta.mypet.domain.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.report.dto.ReportSearchCondition;
import com.sparta.mypet.domain.report.entity.Report;

public interface ReportRepositoryQuery {
	Page<Report> findBySearchCond(ReportSearchCondition condition, Pageable pageable);
}
