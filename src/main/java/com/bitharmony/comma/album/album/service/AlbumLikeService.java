package com.bitharmony.comma.album.album.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.entity.AlbumLike;
import com.bitharmony.comma.album.album.entity.AlbumLikeId;
import com.bitharmony.comma.album.album.repository.AlbumLikeRepository;
import com.bitharmony.comma.album.album.repository.AlbumRepository;
import com.bitharmony.comma.global.exception.AlbumNotFoundException;
import com.bitharmony.comma.global.exception.MemberNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumLikeService {
	private final AlbumRepository albumRepository;
	private final AlbumLikeRepository albumLikeRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public AlbumLike addAlbumLike(Long albumId, Long memberId) {
		Album album = albumRepository.findById(albumId)
			.orElseThrow(() -> new AlbumNotFoundException("존재하지 않는 앨범입니다."));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException("존재하지 않는 유저입니다."));

		// Check if the member has already liked the album
		Optional<AlbumLike> existingLike = albumLikeRepository.findByAlbumAndMember(album, member);
		if (existingLike.isPresent()) {
			throw new IllegalArgumentException("The member has already liked this album");
		}

		AlbumLikeId id = AlbumLikeId.builder()
			.album(albumId)
			.member(memberId)
			.build();

		AlbumLike albumLike = AlbumLike.builder()
			.id(id)
			.album(album)
			.member(member)
			.build();

		return albumLikeRepository.save(albumLike);
	}
}
