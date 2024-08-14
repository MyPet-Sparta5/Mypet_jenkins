package com.sparta.mypet.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;
import com.sparta.mypet.domain.post.entity.PostStatus;
import com.sparta.mypet.domain.s3.FileResponseDto;

import lombok.Getter;

@Getter
public class PostResponseDto {

	private final Long id;
	private final String title;
	private final String content;
	private final Category category;
	private final Long likeCount;
	private final String nickname;
	private final Long postUserId;
	private final PostStatus postStatus;
	private final LocalDateTime createAt;
	private final boolean isLike;
	private final List<FileResponseDto> files;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.nickname = post.getUser().getNickname();
		this.postUserId = post.getUser().getId();
		this.postStatus = post.getPostStatus();
		this.createAt = post.getCreatedAt();
		this.isLike = false;
		this.files = post.getUploadedFiles().stream().map(FileResponseDto::new).toList();
	}

	public PostResponseDto(Post post, boolean isLike) {
		this.id = post.getId();
		this.title = post.getPostTitle();
		this.content = post.getPostContent();
		this.category = post.getCategory();
		this.likeCount = post.getLikeCount();
		this.nickname = post.getUser().getNickname();
		this.postUserId = post.getUser().getId();
		this.postStatus = post.getPostStatus();
		this.createAt = post.getCreatedAt();
		this.isLike = isLike;
		this.files = post.getUploadedFiles().stream().map(FileResponseDto::new).toList();
	}
}
