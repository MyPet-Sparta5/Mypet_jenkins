package com.sparta.mypet.domain.comment;

import com.sparta.mypet.domain.comment.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
