package com.bitharmony.comma.album.album.service;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.repository.AlbumRepository;
import com.bitharmony.comma.album.file.service.FileService;
import com.bitharmony.comma.album.file.util.FileType;
import com.bitharmony.comma.album.file.util.NcpImageUtil;
import com.bitharmony.comma.global.exception.AlbumNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;
	private final NcpImageUtil ncpImageUtil;

	@Transactional
	public Album release(AlbumCreateRequest request, MultipartFile musicFile, MultipartFile musicImageFile) {
		return saveAlbum(request.toEntity(), musicFile, musicImageFile);
	}

	@Transactional
	public Album edit(AlbumEditRequest request, Album album, MultipartFile musicFile, MultipartFile musicImageFile) {
		album.update(request);

		if (musicFile != null)
			fileService.deleteFile(fileService.getAlbumFileUrl(album.getFilePath()),
				ncpImageUtil.getBucketName());

		if (musicImageFile != null)
			fileService.deleteFile(fileService.getAlbumFileUrl(album.getImagePath()),
				ncpImageUtil.getBucketName());

		saveAlbum(album, musicFile, musicImageFile);
		return album;
	}

	@Transactional
	public void delete(Album album) {
		//fileService.deleteFile(album.getFilePath(), ncpImageUtil.getMusicBucketName());
		fileService.deleteFile(album.getImagePath(), ncpImageUtil.getBucketName());
		albumRepository.delete(album);
	}

	public Album saveAlbum(Album album, MultipartFile musicFile, MultipartFile musicImageFile) {
		//uploadFileAndSetUrl(musicFile, ncpImageUtil.getMusicBucketName(), album::updateFileUrl);
		String fileUrl = fileService.uploadFile(musicFile, ncpImageUtil.getBucketName()).uploadFileUrl();
		album = album.toBuilder().filePath(fileUrl).build();

		albumRepository.save(album);
		return album;
	}

	private void uploadFileAndSetUrl(MultipartFile file, String bucketName, Consumer<String> urlSetter) {
		Optional.ofNullable(file)
			.map(f -> fileService.uploadFile(f, bucketName))
			.ifPresent(response -> urlSetter.accept(response.uploadFileUrl()));
	}

	public Album getAlbumById(long id) {
		return albumRepository.findById(id).orElseThrow(() -> new AlbumNotFoundException("앨범을 찾을 수 없습니다."));
	}

	/**
	 * ncp image optimizer 사용 cdn url을 통해서 변환된 이미지 가져오기
	 */
	private String replaceBucketName(String filepath, String bucketName, String replacement) {
		return filepath.replace(bucketName, replacement);
	}

	public String getAlbumImageUrl(String filepath) {
		if (filepath == null) {
			return "여기에 기본 이미지 URL";
		}

		return ncpImageUtil.getImageCdn() + replaceBucketName(filepath, ncpImageUtil.getBucketName(), "")
			+ ncpImageUtil.getImageCdnQueryString();
	}

	public String getAlbumFileUrl(String filepath) {
		return ncpImageUtil.getEndPoint() + "/" + replaceBucketName(filepath, ncpImageUtil.getBucketName(), "");
	}

	public boolean canRelease(String name, MultipartFile musicFile, MultipartFile musicImageFile, Principal principal) {
		if(principal == null) return false;

		Optional<MultipartFile> audioFile = fileService.checkFileByType(musicFile, FileType.AUDIO);
		Optional<MultipartFile> imgFile = fileService.checkFileByType(musicImageFile, FileType.IMAGE);

		if (albumRepository.findByAlbumname(name).isPresent())
			return false;
		if (audioFile.isEmpty())
			return false;

		return true;
	}

	public boolean canEdit(Album album, Principal principal) {
		if(!album.getMember().getUsername().equals(principal.getName())) return false;
		return true;
	}

	public boolean canDelete(Album album, Principal principal) {
		if(!album.getMember().getUsername().equals(principal.getName())) return false;
		return true;
	}

	public Album getAlbumByFilePath(String filePath) {
		return albumRepository.findByFilePath(filePath).orElseThrow(() -> new AlbumNotFoundException("앨범을 찾을 수 없습니다."));
	}
}