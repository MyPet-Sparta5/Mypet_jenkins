package com.sparta.mypet.domain.post;

import static com.sparta.mypet.domain.post.entity.QPost.*;

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
import com.sparta.mypet.domain.post.dto.PostSearchCondition;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> findBySearchCond(PostSearchCondition searchCondition, Pageable pageable) {

		OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable);

		List<Post> posts = queryFactory.selectFrom(post)
			.where(eqCategory(searchCondition.getCategory())
				, eqStatus(searchCondition.getStatus())
				, containsTitle(searchCondition.getTitle())
				, containsNickname(searchCondition.getNickname())
				, eqEmail(searchCondition.getEmail())
				, betweenDate(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderSpecifier)
			.fetch();

		Long countResult = queryFactory.select(post.count())
			.from(post)
			.where(eqCategory(searchCondition.getCategory())
				, eqStatus(searchCondition.getStatus())
				, containsTitle(searchCondition.getTitle())
				, containsNickname(searchCondition.getNickname())
				, eqEmail(searchCondition.getEmail())
				, betweenDate(searchCondition.getStartDate(), searchCondition.getEndDate()))
			.fetchOne();

		long total = countResult != null ? countResult : 0L;  // null 일 경우 0으로 total

		return new PageImpl<>(posts, pageable, total);
	}

	private BooleanExpression eqCategory(Category category) {
		return category != null ? post.category.eq(category) : null;
	}

	private BooleanExpression eqStatus(PostStatus status) {
		return status != null ? post.postStatus.eq(status) : null;
	}

	private BooleanExpression containsTitle(String title) {
		return StringUtils.hasText(title) ? post.postTitle.containsIgnoreCase(title) : null;
	}

	private BooleanExpression containsNickname(String nickname) {
		return StringUtils.hasText(nickname) ? post.user.nickname.containsIgnoreCase(nickname) : null;
	}

	private BooleanExpression eqEmail(String email) {
		return StringUtils.hasText(email) ? post.user.email.eq(email) : null;
	}

	private BooleanExpression betweenDate(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null) {
			return post.createdAt.between(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
		} else if (startDate != null) {
			return post.createdAt.goe(startDate.atStartOfDay());
		} else if (endDate != null) {
			return post.createdAt.loe(endDate.plusDays(1).atStartOfDay());
		} else {
			return null;
		}
	}

	private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
		Sort.Order order = pageable.getSort().iterator().next();
		PathBuilder<Post> pathBuilder = new PathBuilder<>(post.getType(), post.getMetadata());
		return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC,
			pathBuilder.get(order.getProperty()));
	}
}
