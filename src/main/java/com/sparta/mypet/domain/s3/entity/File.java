package com.sparta.mypet.domain.s3.entity;

import com.sparta.mypet.domain.post.entity.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

	@Column(name = "file_order", nullable = false)
	private int order;

	@Builder
	public File(Post post, String url, String name, int order) {
		this.post = post;
		this.url = url;
		this.name = name;
		this.order = order;
	}

	public void updateUrl(String url) {
		this.url = url;
	}

	public String generateFileKey() {
		return String.format("%d/%d-%s", post.getId(), id, name);
	}
}
