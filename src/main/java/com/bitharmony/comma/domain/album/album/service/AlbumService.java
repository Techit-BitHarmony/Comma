package com.bitharmony.comma.domain.album.album.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.repository.AlbumRepository;
import com.bitharmony.comma.domain.album.file.dto.FileResponse;
import com.bitharmony.comma.domain.album.file.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;

	@Value("${ncp.object-storage.img-folder}")
	private String uploadFolder;

	@Transactional
	public Album release(AlbumCreateRequest request, MultipartFile imgFile) {
		//앨범 생성
		Album album = request.toEntity();
		saveAlbum(album, imgFile);

		album.updateImageUrl(fileService.getAlbumImageUrl(album.getImagePath()));
		return album;
	}

	@Transactional
	public Album edit(AlbumEditRequest request, MultipartFile imgFile, Album album) {
		album.update(request);
		saveAlbum(album, imgFile);

		album.updateImageUrl(fileService.getAlbumImageUrl(album.getImagePath()));
		return album;
	}

	@Transactional
	public void delete(Album album) {
		albumRepository.delete(album);
	}

	public void saveAlbum(Album album, MultipartFile imgFile) {
		FileResponse imgResponse;

		if (imgFile != null) {
			imgResponse = fileService.uploadFile(imgFile, uploadFolder);
			album.updateImageUrl(imgResponse.uploadFileUrl());
		}

		albumRepository.save(album);
	}

	public Optional<Album> getAlbumById(long id) {
		return albumRepository.findById(id);
	}

	public boolean canRelease(String name) {
		//TODO : 등록 가능한 여부 확인
		if (!albumRepository.findByAlbumname(name).isPresent()) {
			return false;
		}
		return true;
	}

	public boolean canEdit(Album album) {
		//TODO : 수정 가능한 여부 확인
		if (album == null) { return false; }
		return true;
	}

	public boolean canDelete(Album album) {
		//TODO : 삭제 가능한 여부 확인
		if (album == null) { return false; }
		return true;
	}
}