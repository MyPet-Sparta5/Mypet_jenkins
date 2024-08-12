package com.sparta.mypet.domain.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.auth.dto.UserSearchCondition;
import com.sparta.mypet.domain.auth.entity.User;

public interface UserRepositoryQuery {
	Page<User> findBySearchCond(UserSearchCondition searchCondition, Pageable pageable);
}
