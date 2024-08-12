package com.sparta.mypet.domain.auth;

import static com.sparta.mypet.domain.auth.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.mypet.domain.auth.entity.QUser;
import com.sparta.mypet.domain.auth.dto.UserSearchCondition;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.suspension.entity.QSuspension;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<User> findBySearchCond(UserSearchCondition searchCondition, Pageable pageable) {

		List<User> users = queryFactory.selectFrom(user)
			.where(eqStatus(searchCondition.getStatus())
				, eqRole(searchCondition.getRole())
				, eqEmail(searchCondition.getEmail()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long countResult = queryFactory.select(user.count())
			.from(user)
			.where(eqStatus(searchCondition.getStatus())
				, eqRole(searchCondition.getRole())
				, eqEmail(searchCondition.getEmail()))
			.fetchOne();

		long total = countResult != null ? countResult : 0L;  // null 일 경우 0으로 total

		return new PageImpl<>(users, pageable, total);
	}

	private BooleanExpression eqStatus(UserStatus status) {
		return status != null ? user.status.eq(status) : null;
	}

	private BooleanExpression eqRole(UserRole role) {
		return role != null ? user.role.eq(role) : null;
	}

	private BooleanExpression eqEmail(String email) {
		return StringUtils.hasText(email) ? user.email.containsIgnoreCase(email) : null;
	}

	@Override
	public List<User> findExpiredSuspendedUsers() {
		QUser user = QUser.user;
		QSuspension suspension = QSuspension.suspension;

		return queryFactory.select(user)
			.from(user)
			.innerJoin(suspension)
			.on(user.id.eq(suspension.user.id)
				.and(suspension.suspensionEndDatetime.eq(
					JPAExpressions.select(suspension.suspensionEndDatetime.max())
						.from(suspension)
						.where(suspension.user.id.eq(user.id))
				)))
			.where(user.status.eq(UserStatus.SUSPENSION)
				.and(suspension.suspensionEndDatetime.before(LocalDateTime.now())))
			.fetch();
	}
}
