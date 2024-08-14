package com.sparta.mypet.domain.suspension;

import static com.sparta.mypet.domain.suspension.entity.QSuspension.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.mypet.domain.suspension.dto.SuspensionSearchCondition;
import com.sparta.mypet.domain.suspension.entity.Suspension;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SuspensionRepositoryQueryImpl implements SuspensionRepositoryQuery {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Suspension> findBySearchCond(SuspensionSearchCondition searchCondition, Pageable pageable) {
		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable);

		List<Suspension> suspensions = queryFactory.selectFrom(suspension)
			.where(containsEmail(searchCondition.getEmail())
				, betweenSuspensionDates(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		Long countResult = queryFactory.select(suspension.count())
			.from(suspension)
			.where(containsEmail(searchCondition.getEmail())
				, betweenSuspensionDates(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.fetchOne();

		long total = countResult != null ? countResult : 0L;  // null 일 경우 0으로 total

		return new PageImpl<>(suspensions, pageable, total);
	}

	private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
		Sort.Order order = pageable.getSort().iterator().next();
		PathBuilder<Suspension> pathBuilder = new PathBuilder<>(suspension.getType(), suspension.getMetadata());
		return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
			pathBuilder.getComparable(order.getProperty(), Comparable.class));
	}

	private BooleanExpression containsEmail(String email) {
		return StringUtils.hasText(email) ? suspension.user.email.containsIgnoreCase(email) : null;
	}

	private BooleanExpression betweenSuspensionDates(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null) {
			return suspension.suspensionStartDatetime.loe(endDate.atTime(23, 59, 59))
				.and(suspension.suspensionEndDatetime.goe(startDate.atStartOfDay()));
		} else if (startDate != null) {
			return suspension.suspensionEndDatetime.goe(startDate.atStartOfDay());
		} else if (endDate != null) {
			return suspension.suspensionStartDatetime.loe(endDate.atTime(23, 59, 59));
		} else {
			return null;
		}
	}

}
