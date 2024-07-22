package com.sparta.mypet.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.mypet.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
