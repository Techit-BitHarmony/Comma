package com.bitharmony.comma.domain.album.album.entity;

import java.util.List;

import com.bitharmony.comma.domain.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.domain.album.albumLike.entity.AlbumLike;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private Long id;

	// @ManyToOne(name = "member_id")(fetch = FetchType.LAZY)
	// private Member member;

	@Column(nullable = false, length = 50)
	private String albumname;

	@Column(nullable = false, length = 20)
	private String genre;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean license;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String licenseDescription;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String filePath;

	@Column(columnDefinition = "TEXT")
	private String imagePath;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean permit;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int price;

	@OneToMany(fetch = FetchType.LAZY)
	private List<AlbumLike> albumLike;

	public void updateImageUrl(String url) { this.imagePath = url; }

	public void updateFileUrl(String url) { this.filePath = url; }

	public void update(AlbumEditRequest request) {
		this.albumname = request.albumname();
		this.genre = request.genre();
		this.license = request.license();
		this.licenseDescription = request.licenseDescription();
		this.permit = request.permit();
		this.price = request.price();
	}
}
