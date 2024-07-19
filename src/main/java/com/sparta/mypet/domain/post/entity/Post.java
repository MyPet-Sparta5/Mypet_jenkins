package com.sparta.mypet.domain.post.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.mypet.common.Timestamped;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.comment.entity.Comment;
import com.sparta.mypet.domain.like.entity.Like;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "posts")
@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(nullable = false)
	private String postTitle;

	@Column(nullable = false)
	private String postContent;

	@Column(nullable = false)
	private String category;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private Long likeCount;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Like> likes = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<File> files = new ArrayList<>();

	@Builder
	public Post(String postContent, String postTitle, String category, User user, Long likeCount) {
		this.postContent = postContent;
		this.postTitle = postTitle;
		this.category = category;
		this.user = user;
		this.likeCount = likeCount;
	}

	public void addComment(Comment comment){
		this.comments.add(comment);
	}

	public void addLike(Like like){
		this.likes.add(like);
		likeCount++;
	}

	public void removeLike(Like like){
		this.likes.remove(like);
		likeCount--;
	}

	public void addFiles(File file){
		this.files.add(file);
	}
}
