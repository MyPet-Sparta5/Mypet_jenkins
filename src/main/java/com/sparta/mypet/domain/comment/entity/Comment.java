package com.sparta.mypet.domain.comment.entity;

import com.sparta.mypet.common.entity.Timestamped;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.post.entity.Post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@Column(nullable = false)
	private String content;

	@Builder
	public Comment(User user, Post post, String content) {
		this.user = user;
		this.post = post;
		this.content = content;
	}
}
