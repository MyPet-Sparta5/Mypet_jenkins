package com.sparta.mypet.domain.suspension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.suspension.dto.SuspensionSearchCondition;
import com.sparta.mypet.domain.suspension.entity.Suspension;

public interface SuspensionRepositoryQuery {
	Page<Suspension> findBySearchCond(SuspensionSearchCondition condition, Pageable pageable);
}
