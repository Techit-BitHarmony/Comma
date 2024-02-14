package com.bitharmony.comma.album.album.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.repository.AlbumRepository;
import com.bitharmony.comma.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumLikeService {
	private final AlbumRepository albumRepository;

	@Transactional
	public void like(Member actor, Album album) {
		if (album.getAlbumLikes().contains(actor)) {
			album.getAlbumLikes().remove(actor);
			album.decreaseLikesCount();
		} else {
			album.getAlbumLikes().add(actor);
			album.increaseLikesCount();
		}
		this.albumRepository.save(album);
	}

	public Boolean canLike(Member member, Album album) {
		if (member == null) return false;
		if (album == null) return false;
		if (album.getAlbumLikes().contains(member)) return false;

		return true;
	}

	public Boolean canCancelLike(Member member, Album album) {
		if (member == null) return false;
		if (album == null) return false;
		if (!album.getAlbumLikes().contains(member)) return false;

		return true;
	}
}
