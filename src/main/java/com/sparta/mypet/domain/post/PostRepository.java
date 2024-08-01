package com.sparta.mypet.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findByCategory(Category category, Pageable pageable);

	Page<Post> findByPostStatus(PostStatus status, Pageable pageable);

	Page<Post> findByCategoryAndPostStatus(Category category, PostStatus status, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.user.email = :email AND p.postStatus = :postStatus" )
	Page<Post> findByUserName(@Param("email") String email, @Param("postStatus") String postStatus, Pageable pageable);
}
