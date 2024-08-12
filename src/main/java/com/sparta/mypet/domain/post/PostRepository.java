package com.sparta.mypet.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {

	Page<Post> findByCategory(Category category, Pageable pageable);

	Page<Post> findByPostStatus(PostStatus status, Pageable pageable);

	Page<Post> findByCategoryAndPostStatus(Category category, PostStatus status, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.user.email = :email AND p.postStatus = :postStatus")
	Page<Post> findByUserName(@Param("email") String email, @Param("postStatus") PostStatus postStatus,
		Pageable pageable);

	@Query(value = "SELECT p.post_id " +
		"FROM reports r " +
		"LEFT JOIN posts p ON r.reported_post_id = p.post_id " +
		"WHERE p.user_id = :userId",
		nativeQuery = true)
	List<Long> findReportedPostIdsByUserId(@Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query("UPDATE Post p SET p.postStatus = :status WHERE p.id IN :postIds AND p.id NOT IN " +
		"(SELECT r.reportedPost.id FROM Report r WHERE r.reportStatus = 'REJECTED')")
	void updatePostStatus(@Param("status") PostStatus status, @Param("postIds") List<Long> postIds);

}
