package com.sparta.mypet.domain.s3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.s3.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
