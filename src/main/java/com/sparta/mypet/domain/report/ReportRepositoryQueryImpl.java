package com.sparta.mypet.domain.report;

import static com.sparta.mypet.domain.report.entity.QReport.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.mypet.domain.post.entity.QPost;
import com.sparta.mypet.domain.report.dto.ReportSearchCondition;
import com.sparta.mypet.domain.report.entity.QReport;
import com.sparta.mypet.domain.report.entity.Report;
import com.sparta.mypet.domain.report.entity.ReportStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryQueryImpl implements ReportRepositoryQuery {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Report> findBySearchCond(ReportSearchCondition searchCondition, Pageable pageable) {
		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable);

		List<Report> reports = queryFactory.selectFrom(report)
			.where(eqStatus(searchCondition.getStatus())
				, containsEmail(searchCondition.getEmail())
				, betweenDate(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		Long countResult = queryFactory.select(report.count())
			.from(report)
			.where(eqStatus(searchCondition.getStatus())
				, containsEmail(searchCondition.getEmail())
				, betweenDate(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.fetchOne();

		long total = countResult != null ? countResult : 0L;  // null 일 경우 0으로 total

		return new PageImpl<>(reports, pageable, total);
	}

	private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
		Sort.Order order = pageable.getSort().iterator().next();
		PathBuilder<Report> pathBuilder = new PathBuilder<>(report.getType(), report.getMetadata());
		return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
			pathBuilder.getComparable(order.getProperty(), Comparable.class));
	}

	private BooleanExpression containsEmail(String email) {
		return StringUtils.hasText(email) ? report.reportedPost.user.email.containsIgnoreCase(email) : null;
	}

	private BooleanExpression eqStatus(ReportStatus status) {
		return status != null ? report.reportStatus.eq(status) : null;
	}

	private BooleanExpression betweenDate(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null) {
			return report.createdAt.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
		} else if (startDate != null) {
			return report.createdAt.goe(startDate.atStartOfDay());
		} else if (endDate != null) {
			return report.createdAt.loe(endDate.plusDays(1).atStartOfDay());
		} else {
			return null;
		}
	}

	@Override
	public List<Long> findReportIdsByPostUserId(Long userId) {
		QReport report = QReport.report;
		QPost post = QPost.post;

		return queryFactory.select(report.id)
			.from(report)
			.leftJoin(post)
			.on(report.reportedPost.id.eq(post.id))
			.where(post.user.id.eq(userId)
				.and(report.reportStatus.in(ReportStatus.IN_PROGRESS, ReportStatus.PENDING)))
			.fetch();
	}

	@Override
	@Transactional
	public void updateReportStatusAndHandleUserIdByReportIds(Long handleUserId, List<Long> reportIds) {
		QReport report = QReport.report;

		queryFactory.update(report)
			.set(report.reportStatus, ReportStatus.COMPLETED)
			.set(report.handleUserId, handleUserId)
			.where(report.id.in(reportIds))
			.execute();
	}
}
