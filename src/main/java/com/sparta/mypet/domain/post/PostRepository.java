package com.sparta.mypet.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findByCategory(Category category, Pageable pageable);
}
