package com.sparta.mypet.domain.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserSearchCondition;

public interface UserRepositoryQuery {
	Page<User> findBySearchCond(UserSearchCondition searchCondition, Pageable pageable);
}
