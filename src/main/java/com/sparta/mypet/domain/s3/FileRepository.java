package com.sparta.mypet.domain.s3;

import java.nio.file.Files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

	@Query("SELECT COUNT(f) FROM File f WHERE f.post.id = :postId")
	int countFilesByPostId(@Param("postId") Long postId);

}
