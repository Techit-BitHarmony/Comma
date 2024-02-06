package com.bitharmony.comma.album.album.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.entity.AlbumLike;
import com.bitharmony.comma.album.album.entity.AlbumLikeId;
import com.bitharmony.comma.member.entity.Member;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, AlbumLikeId> {
	boolean existsByIdMemberAndIdAlbum(Member member, Album album);
}