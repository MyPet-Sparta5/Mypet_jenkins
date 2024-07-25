package com.sparta.mypet.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.report.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
