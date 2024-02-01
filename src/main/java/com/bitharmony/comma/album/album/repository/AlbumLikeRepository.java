package com.bitharmony.comma.album.album.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.entity.AlbumLike;
import com.bitharmony.comma.member.entity.Member;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {
	Optional<AlbumLike> findByMemberAndAlbum(Member member, Album album);
}
