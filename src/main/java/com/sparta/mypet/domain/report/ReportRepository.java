package com.sparta.mypet.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.mypet.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
