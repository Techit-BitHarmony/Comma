package com.bitharmony.comma.album.album.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.album.album.dto.AlbumListResponse;
import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.exception.AlbumNotFoundException;
import com.bitharmony.comma.album.album.repository.AlbumRepository;
import com.bitharmony.comma.album.file.service.FileService;
import com.bitharmony.comma.album.file.util.FileType;
import com.bitharmony.comma.album.file.util.NcpImageUtil;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.streaming.util.NcpMusicUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final FileService fileService;
	private final NcpImageUtil ncpImageUtil;
	private final NcpMusicUtil ncpMusicUtil;

	@Transactional
	public Album release(AlbumCreateRequest request, Member member, MultipartFile musicImageFile) {
		Album album = request.toEntity();
		album.updateReleaseMember(member);
		return saveAlbum(album, musicImageFile);
	}

	@Transactional
	public Album edit(AlbumEditRequest request, Album album, MultipartFile musicImageFile) {
		album.update(request);

		if (musicImageFile != null && album.getFilePath() != null)
			fileService.deleteFile(fileService.getAlbumFileUrl(album.getImagePath()), ncpImageUtil.getBucketName());

		saveAlbum(album, musicImageFile);
		return album;
	}

	@Transactional
	public void delete(Album album) {
		ncpMusicUtil.deleteFile(album.getFilePath());
		fileService.deleteFile(album.getImagePath(), ncpImageUtil.getBucketName());
		albumRepository.delete(album);
	}

	public Album saveAlbum(Album album, MultipartFile musicImageFile) {
		if (musicImageFile != null) {
			String imageUrl = fileService.uploadFile(musicImageFile, ncpImageUtil.getBucketName()).uploadFileUrl();
			album = album.toBuilder().imagePath(imageUrl).build();
		}

		albumRepository.save(album);
		return album;
	}

	public Album getAlbumById(long id) {
		return albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
	}

	public List<AlbumListResponse> getLatest20Albums(String username) {
		Pageable topTwenty = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));
		Page<Album> albums = albumRepository.findFirst20ByMemberUsernameOrderByIdDesc(username, topTwenty);
		return albums.stream()
			.map(this::convertToDto)
			.collect(Collectors.toList());
	}

	public AlbumListResponse convertToDto(Album album) {
		return AlbumListResponse.builder()
			.id(album.getId())
			.albumname(album.getAlbumname())
			.genre(album.getGenre())
			.imgPath(getAlbumImageUrl(album.getImagePath()))
			.permit(album.isPermit())
			.price(album.getPrice())
			.artistUsername(album.getMember().getUsername())
			.artistNickname(album.getMember().getNickname())
			.build();
	}

	private String replaceBucketName(String filepath, String bucketName, String replacement) {
		return filepath.replace(bucketName, replacement);
	}

	public String getAlbumImageUrl(String filepath) {
		if (filepath == null) {
			return null;
		}

		return ncpImageUtil.getImageCdn() + replaceBucketName(filepath, ncpImageUtil.getBucketName(), "")
			+ ncpImageUtil.getImageCdnQueryString();
	}

	public String getAlbumFileUrl(String filepath) {
		return ncpImageUtil.getEndPoint() + "/" + replaceBucketName(filepath, ncpImageUtil.getBucketName(), "");
	}

	public boolean canRelease(String name, MultipartFile musicImageFile, Member member) {
		if (member == null)
			return false;
		if (albumRepository.findByAlbumname(name).isPresent())
			return false;

		Optional<MultipartFile> imgFile = fileService.checkFileByType(musicImageFile, FileType.IMAGE);
		if (imgFile.isEmpty() && musicImageFile != null)
			return false;

		return true;
	}

	public boolean canEdit(Album album, Principal principal, MultipartFile musicImageFile,
		@Valid AlbumEditRequest request, Member member) {
		if (member == null)
			return false;
		if (!album.getMember().getUsername().equals(principal.getName()))
			return false;
		if (albumRepository.findByAlbumname(request.albumname()).isPresent())
			return false;

		Optional<MultipartFile> imgFile = fileService.checkFileByType(musicImageFile, FileType.IMAGE);
		if (imgFile.isEmpty() && musicImageFile != null)
			return false;

		return true;
	}

	public boolean canDelete(Album album, Principal principal) {
		if (!album.getMember().getUsername().equals(principal.getName()))
			return false;
		return true;
	}

	public Album getAlbumByFilePath(String filePath) {
		return albumRepository.findByFilePath(filePath).orElseThrow(AlbumNotFoundException::new);
	}
}