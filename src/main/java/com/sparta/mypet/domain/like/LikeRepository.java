package com.sparta.mypet.domain.like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.entity.Like;
import com.sparta.mypet.domain.post.entity.Post;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

	boolean existsByUserAndPost(User user, Post post);

	Optional<Like> findByUserAndPost(User user, Post post);
}
