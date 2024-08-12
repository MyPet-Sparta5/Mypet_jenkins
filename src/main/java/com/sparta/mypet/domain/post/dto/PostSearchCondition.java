package com.sparta.mypet.domain.post.dto;

import java.time.LocalDate;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.PostStatus;

import lombok.Data;

@Data
public class PostSearchCondition {
	private Category category;
	private PostStatus status;
	private String title;
	private String nickname;
	private LocalDate startDate;
	private LocalDate endDate;
}
