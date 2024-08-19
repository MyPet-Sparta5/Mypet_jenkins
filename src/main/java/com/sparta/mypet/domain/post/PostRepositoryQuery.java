package com.sparta.mypet.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.mypet.domain.post.dto.PostSearchCondition;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;

public interface PostRepositoryQuery {
	Page<Post> findBySearchCond(PostSearchCondition condition, Pageable pageable);

	List<Long> findReportedPostIdsByUserId(Long userId);

	void updatePostStatus(PostStatus status, List<Long> postIds);
}
