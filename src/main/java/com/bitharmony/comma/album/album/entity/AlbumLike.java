package com.bitharmony.comma.album.album.entity;

import com.bitharmony.comma.member.entity.Member;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumLike {
	@EmbeddedId
	private AlbumLikeId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", insertable = false, updatable = false)
	private Album album;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	private Member member;
}