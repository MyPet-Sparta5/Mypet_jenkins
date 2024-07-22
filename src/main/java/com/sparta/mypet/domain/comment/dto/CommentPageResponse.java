package com.sparta.mypet.domain.comment.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CommentPageResponse {
	private final List<CommentResponseDto> comments;
	private final PageInfo pageInfo;

	@Getter
	public static class PageInfo {
		private final int pageNumber;
		private final int totalPages;
		private final int pageSize;
		private final long totalElements;
		private final boolean hasNext;
		private final boolean hasPrevious;

		public PageInfo(int pageNumber, int pageSize, int totalPages, long totalElements, boolean hasNext,
			boolean hasPrevious) {
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.totalPages = totalPages;
			this.totalElements = totalElements;
			this.hasNext = hasNext;
			this.hasPrevious = hasPrevious;
		}
	}

	public CommentPageResponse(List<CommentResponseDto> comments, PageInfo pageInfo) {
		this.comments = comments;
		this.pageInfo = pageInfo;
	}
}
