package com.sparta.mypet.domain.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.mypet.domain.comment.dto.CommentResponseDto;
import com.sparta.mypet.domain.comment.entity.Comment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;

	public List<CommentResponseDto> getComments(Long postId) {

		List<Comment> commentList = commentRepository.findAllByPostId(postId);
		return commentList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
	}

}
