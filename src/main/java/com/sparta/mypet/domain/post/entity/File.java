package com.sparta.mypet.domain.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "files")
@Entity
@Getter
@NoArgsConstructor
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@Column(name = "file_url", nullable = false)
	private String url;

	@Column(name = "file_name", nullable = false)
	private String name;

	@Builder
	public File(Post post, String url, String name) {
		this.post = post;
		this.url = url;
		this.name = name;
	}
}
