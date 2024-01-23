package com.bitharmony.comma.domain.album.album.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.repository.AlbumRepository;
import com.bitharmony.comma.domain.album.file.dto.FileResponse;
import com.bitharmony.comma.domain.album.file.service.FileService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void release(AlbumCreateRequest request, MultipartFile imgFile) {
		//멤버 조회
		//멤버가 없으면 에러

		//같은 앨범 이름 조회
		//같은 앨범 이름이 있으면 에러
		if (albumRepository.findByAlbumname(request.albumname()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 앨범 이름입니다.");
		}

		//앨범 생성
		Album album = request.toEntity();

		//이미지
		if (imgFile != null) {
			FileResponse imgResponse = fileService.uploadFile(imgFile, uploadFolder);
			album.updateImageUrl(imgResponse.uploadFileUrl());
		}

		albumRepository.save(album);
		//리턴?
	}
}
