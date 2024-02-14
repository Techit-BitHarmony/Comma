package com.bitharmony.comma.album.album.entity;

import static jakarta.persistence.CascadeType.*;

import java.util.HashSet;
import java.util.Set;

import com.bitharmony.comma.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@NotNull
	private Member member;

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

	@ManyToMany
	private Set<Member> albumLikes = new HashSet<>();

	private long albumLikesCount;

	@OneToMany(mappedBy = "album", cascade = ALL, orphanRemoval = true)
	@Builder.Default
	private Set<StreamingCount> streamingCounts = new HashSet<>();

	public void update(AlbumEditRequest request) {
		this.albumname = request.albumname();
		this.genre = request.genre();
		this.license = request.license();
		this.licenseDescription = request.licenseDescription();
		this.permit = request.permit();
		this.price = request.price();
	}

	public void updateReleaseMember(Member member) {
		this.member = member;
	}

	public void increaseLikesCount() {
		albumLikesCount++;
	}

	public void decreaseLikesCount() {
		albumLikesCount--;
	}
}