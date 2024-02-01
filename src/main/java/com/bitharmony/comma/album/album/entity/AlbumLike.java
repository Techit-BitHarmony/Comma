package com.bitharmony.comma.album.album.entity;

import static lombok.AccessLevel.*;

import com.bitharmony.comma.member.entity.Member;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@Entity
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class AlbumLike {
	@EmbeddedId
	@Delegate
	private AlbumLikeId id;

	@Builder
	private static AlbumLike of(Album album, Member member) {
		return new AlbumLike(
				AlbumLikeId.builder()
						.album(album)
						.member(member)
						.build()
		);
	}
}