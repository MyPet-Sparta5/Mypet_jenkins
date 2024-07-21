package com.sparta.mypet.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.mypet.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByPostId(Long postId);
}
