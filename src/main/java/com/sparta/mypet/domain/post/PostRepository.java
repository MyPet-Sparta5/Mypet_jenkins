package com.sparta.mypet.domain.post;

import com.sparta.mypet.domain.post.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
