package com.sparta.mypet.domain.like;

import java.util.Optional;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.like.entity.Like;
import com.sparta.mypet.domain.post.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

	boolean existsByUserAndPost(User user, Post post);

	Optional<Like> findByUserAndPost(User user, Post post);
}
