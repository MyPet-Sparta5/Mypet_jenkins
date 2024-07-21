package com.sparta.mypet.domain.comment;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.PostNotFoundException;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
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
		User user = getUserByEmail(email);

		Post post = getPostById(postId);

		Comment comment = createAndSaveComment(user, post, requestDto.getContent());

		post.addComment(comment);

		return new CommentResponseDto(comment);
	}

	public List<CommentResponseDto> getComments(Long postId) {

		List<Comment> commentList = commentRepository.findAllByPostId(postId);
		return commentList.stream().map(CommentResponseDto::new).toList();
	}

	private User getUserByEmail(String email) { // request to user service
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}

	private Post getPostById(Long postId) { // request to post service
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(GlobalMessage.POST_NOT_FOUND.getMessage()));
	}

	private Comment createAndSaveComment(User user, Post post, String content) {
		Comment comment = Comment.builder().user(user).post(post).content(content).build();
		return commentRepository.save(comment);
	}

}
