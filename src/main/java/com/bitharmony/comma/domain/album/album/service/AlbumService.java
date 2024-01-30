package com.bitharmony.comma.domain.album.album.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.repository.AlbumRepository;
import com.bitharmony.comma.domain.album.file.dto.FileResponse;
import com.bitharmony.comma.domain.album.file.service.FileService;
import com.bitharmony.comma.domain.album.file.util.FileType;
import com.bitharmony.comma.domain.album.file.util.NcpConfig;
import com.bitharmony.comma.domain.album.file.util.NcpProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;
	private final NcpConfig ncpConfig;
	private final NcpProperties ncpProperties;

	@Transactional
	public Album release(AlbumCreateRequest request, MultipartFile musicFile, MultipartFile musicImageFile) {
		return saveOrUpdateAlbum(request.toEntity(), musicFile, musicImageFile, false);
	}

	@Transactional
	public Album edit(AlbumEditRequest request, Album album, MultipartFile musicFile, MultipartFile musicImageFile) {
		if (musicFile != null)
			fileService.deleteFile(fileService.getAlbumFileUrl(album.getFilePath()),
				ncpProperties.getMusicBucketName());

		if (album.getImagePath() != null && musicImageFile != null)
			fileService.deleteFile(fileService.getAlbumFileUrl(album.getImagePath()),
				ncpProperties.getImageBucketName());

		album.update(request);
		saveOrUpdateAlbum(album, musicFile, musicImageFile, true);
		return album;
	}

	@Transactional
	public void delete(Album album) {
		//TODO ncp에 있는 파일도 삭제해야됨.
		fileService.deleteFile(album.getFilePath(), ncpProperties.getMusicBucketName());
		fileService.deleteFile(album.getImagePath(), ncpProperties.getImageBucketName());
		albumRepository.delete(album);
	}

	public Album saveOrUpdateAlbum(Album album, MultipartFile musicFile, MultipartFile musicImageFile,
		boolean isUpdate) {

		if (musicFile != null) {
			if (isUpdate) {
				fileService.updateFile(musicFile, album.getFilePath(), ncpProperties.getMusicBucketName());
			} else {
				FileResponse audioResponse = fileService.uploadFile(musicFile, ncpProperties.getMusicBucketName());
				album.updateFileUrl(audioResponse.uploadFileUrl());
			}
		}

		if (musicImageFile != null) {
			FileResponse imgResponse = fileService.uploadFile(musicImageFile, ncpProperties.getImageBucketName());
			album.updateImageUrl(imgResponse.uploadFileUrl());
		}

		albumRepository.save(album);
		return album;
	}

	public Optional<Album> getAlbumById(long id) {
		return albumRepository.findById(id);
	}

	/**
	 * ncp image optimizer 사용 cdn url을 통해서 변환된 이미지 가져오기
	 */
	public String getAlbumImageUrl(String filepath) {
		String replacedFilePath = filepath.replace(ncpProperties.getImageBucketName(), "");
		return ncpProperties.getCdnUrl() + replacedFilePath + ncpProperties.getCdnQueryString();
	}

	public String getAlbumFileUrl(String filepath) {
		String replacedFilePath = filepath.replace(ncpProperties.getImageBucketName(), "");
		return ncpConfig.getEndPoint() + "/" + replacedFilePath;
	}

	public boolean canRelease(String name, MultipartFile musicFile, MultipartFile musicImageFile) {
		//TODO : 등록 가능한 여부 확인
		Optional<MultipartFile> audioFile = fileService.checkFileByType(musicFile, FileType.AUDIO);

		if (audioFile.isEmpty()) {
			return false;
		}

		if (albumRepository.findByAlbumname(name).isPresent()) {
			return false;
		}
		return true;
	}

	public boolean canEdit(Album album) {
		//TODO : 수정 가능한 여부 확인
		if (album == null) {
			return false;
		}
		return true;
	}

	public boolean canDelete(Album album) {
		//TODO : 삭제 가능한 여부 확인
		if (album == null) {
			return false;
		}
		return true;
	}
}