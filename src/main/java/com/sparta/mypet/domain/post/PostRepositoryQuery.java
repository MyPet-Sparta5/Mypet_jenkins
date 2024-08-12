package com.sparta.mypet.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.post.dto.PostSearchCondition;
import com.sparta.mypet.domain.post.entity.Post;

public interface PostRepositoryQuery {
	Page<Post> findBySearchCond(PostSearchCondition condition, Pageable pageable);
}
