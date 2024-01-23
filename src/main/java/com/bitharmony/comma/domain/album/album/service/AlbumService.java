package com.bitharmony.comma.domain.album.album.service;

import org.springframework.stereotype.Service;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.repository.AlbumRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;

	@Transactional
	public void create(AlbumCreateRequest request) {
		//멤버 조회
		//멤버가 없으면 에러

		//같은 앨범 이름 조회
		//같은 앨범 이름이 있으면 에러
		if(albumRepository.findByAlbumname(request.albumname()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 앨범 이름입니다.");
		}

		//앨범 생성
		Album album = request.toEntity();

		//앨범 저장
		albumRepository.save(album);
		//리턴?
	}
}
