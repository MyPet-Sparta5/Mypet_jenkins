package com.sparta.mypet.domain.comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.CommentNotFoundException;
import com.sparta.mypet.common.exception.PostNotFoundException;
import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.comment.dto.CommentPageResponse;
import com.sparta.mypet.domain.comment.dto.CommentRequestDto;
import com.sparta.mypet.domain.comment.dto.CommentResponseDto;
import com.sparta.mypet.domain.comment.entity.Comment;
import com.sparta.mypet.domain.post.PostRepository;
import com.sparta.mypet.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	@Transactional
	public CommentResponseDto createComment(String email, Long postId, CommentRequestDto requestDto) {

		User user = findUserByEmail(email);

		Post post = findPostById(postId);

		Comment comment = createAndSaveComment(user, post, requestDto.getContent());
		post.addComment(comment);

		return new CommentResponseDto(comment);
	}

	public CommentPageResponse getComments(Long postId, int page, int pageSize, String sortBy) {

		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

		List<CommentResponseDto> responseDtoList = commentPage.getContent()
			.stream()
			.map(CommentResponseDto::new)
			.toList();

		CommentPageResponse.PageInfo pageInfo = new CommentPageResponse.PageInfo(pageable.getPageNumber(),
			pageable.getPageSize(), commentPage.getTotalPages(), commentPage.getTotalElements(), commentPage.hasNext(),
			pageable.hasPrevious());

		return new CommentPageResponse(responseDtoList, pageInfo);
	}

	public void deleteComment(String email, Long commentId) {

		User user = findUserByEmail(email);

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentNotFoundException(GlobalMessage.COMMENT_NOT_FOUND.getMessage()));

		if (!comment.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException(GlobalMessage.NOT_COMMENT_OWNER.getMessage());
		}

		commentRepository.delete(comment);
	}

	private User findUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}

	private Post findPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	private Comment createAndSaveComment(User user, Post post, String content) {
		Comment comment = Comment.builder().user(user).post(post).content(content).build();
		return commentRepository.save(comment);
	}

}
