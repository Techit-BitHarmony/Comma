package com.bitharmony.comma.domain.album.album.service;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.repository.AlbumRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;

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

		System.out.println(request.albumname());

		//앨범 생성
		Album album = request.toEntity();

		//이미지
		if (imgFile != null) {

			UUID uuid = UUID.randomUUID();
			String imageFileName = uuid + "_" + imgFile.getOriginalFilename();

			File destinationFile = new File(uploadFolder + imageFileName);

			try {
				imgFile.transferTo(destinationFile);
				album.updateImageUrl(imageFileName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		albumRepository.save(album);
		//리턴?
	}
}
