package com.bitharmony.comma.album.album.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.entity.AlbumLike;
import com.bitharmony.comma.album.album.entity.AlbumLikeId;
import com.bitharmony.comma.member.entity.Member;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, AlbumLikeId> {
	Optional<AlbumLike> findByAlbumAndMember(Album album, Member member);

	boolean existsByMemberAndAlbum(Member actor, Album album);
}
