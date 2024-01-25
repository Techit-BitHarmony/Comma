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
import com.bitharmony.comma.domain.album.file.util.NcpProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;
	private final NcpProperties ncpProperties;

	@Transactional
	public Album release(AlbumCreateRequest request, MultipartFile imgFile) {
		//앨범 생성
		Album album = request.toEntity();
		saveAlbum(album, imgFile);

		album.updateImageUrl(getAlbumImageUrl(album.getImagePath()));
		return album;
	}

	@Transactional
	public Album edit(AlbumEditRequest request, MultipartFile imgFile, Album album) {
		album.update(request);
		saveAlbum(album, imgFile);

		album.updateImageUrl(getAlbumImageUrl(album.getImagePath()));
		return album;
	}

	@Transactional
	public void delete(Album album) {
		albumRepository.delete(album);
	}

	public void saveAlbum(Album album, MultipartFile imgFile) {
		FileResponse imgResponse;

		if (imgFile != null) {
			imgResponse = fileService.uploadFile(imgFile, ncpProperties.getUploadFolder());
			album.updateImageUrl(imgResponse.uploadFileUrl());
		}

		albumRepository.save(album);
	}

	public Optional<Album> getAlbumById(long id) {
		return albumRepository.findById(id);
	}

	/**
	 * ncp image optimizer 사용 cdn url을 통해서 변환된 이미지 가져오기
	 */
	public String getAlbumImageUrl(String filepath) {
		String replacedFilePath = filepath.replace(ncpProperties.getBucketName(), "");

		//이미지 URL검증 필요시
		// try {
		// 	// 이미지 URL을 검증하는 코드를 추가합니다.
		// 	// 이 부분은 실제 이미지 URL을 검증하는 로직에 따라 달라집니다.
		// 	validateImageUrl(optimizedImageUrl);
		// } catch (FileNotFoundException e) {
		// 	// 적절한 에러 메시지를 설정하거나 다른 조치를 취합니다.
		// 	System.out.println("The requested image does not exist: " + optimizedImageUrl);
		// 	return null; // 또는 적절한 에러 응답을 반환합니다.
		// }

		return ncpProperties.getCdnUrl() + replacedFilePath + ncpProperties.getCdnQueryString();
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