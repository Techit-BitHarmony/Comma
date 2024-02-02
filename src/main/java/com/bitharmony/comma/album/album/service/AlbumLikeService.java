package com.bitharmony.comma.album.album.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.repository.AlbumLikeRepository;
import com.bitharmony.comma.album.album.repository.AlbumRepository;
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
	public void like(Member actor, Album album) {
		album.addLike(actor);
	}

	@Transactional
	public void cancelLike(Member actor, Album album) {
		album.deleteLike(actor);
	}

	public Boolean canLike(Member member, Album album) {
		if (member == null) return false;
		if (album == null) return false;

		// 이미 앨범에 있는가?
		return !albumLikeRepository.existsByMemberAndAlbum(member, album);
	}

	public Boolean canCancelLike(Member member, Album album) {
		if (member == null) return false;
		if (album == null) return false;
		// 이미 앨범에 있는가?
		return albumLikeRepository.existsByMemberAndAlbum(member, album);
	}
}
