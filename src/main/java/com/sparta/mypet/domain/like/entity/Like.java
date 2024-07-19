package com.sparta.mypet.domain.like.entity;

import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.post.entity.Post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "likes")
@Entity
@Getter
@NoArgsConstructor
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "like_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	public Like(User user, Post post) {
		this.user = user;
		this.post = post;
	}
}
