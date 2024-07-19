package com.sparta.mypet.domain.like;

import com.sparta.mypet.domain.like.entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
