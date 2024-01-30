package com.bitharmony.comma.domain.album.album.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.domain.album.album.dto.AlbumResponse;
import com.bitharmony.comma.domain.album.album.entity.Album;
import com.bitharmony.comma.domain.album.album.service.AlbumService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

	private final AlbumService albumService;

	@GetMapping("/release")
	public String showAlbumForm() {
		return "domain/album/album_form";
	}

	@PostMapping("/release")
	public ResponseEntity<AlbumResponse> releaseAlbum(@Valid AlbumCreateRequest request,
		@RequestParam("musicFile") MultipartFile musicFile,
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile) {

		if (!albumService.canRelease(request.albumname(), musicFile, musicImageFile)) {
			throw new IllegalArgumentException("앨범을 등록할 수 없습니다.");
		}

		Album album = albumService.release(request, musicFile, musicImageFile);
		return new ResponseEntity<>(albumToResponseDto(album), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumResponse> getAlbum(@PathVariable long id) {
		Album album = albumService.getAlbumById(id);

		return new ResponseEntity<>(albumToResponseDto(album), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AlbumResponse> editAlbum(@PathVariable long id, @Valid AlbumEditRequest request,
		@RequestParam(value = "musicFile", required = false) MultipartFile musicFile,
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile) {
		Album album = albumService.getAlbumById(id);

		if (!albumService.canEdit(album)) {
			throw new IllegalArgumentException("앨범을 수정할 수 없습니다.");
		}

		Album editedAlbum = albumService.edit(request, album, musicFile, musicImageFile);
		return new ResponseEntity<>(albumToResponseDto(editedAlbum), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAlbum(@PathVariable long id) {
		Album album = albumService.getAlbumById(id);

		albumService.delete(album);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private AlbumResponse albumToResponseDto(Album album) {
		album.updateFileUrl(albumService.getAlbumFileUrl(album.getFilePath()));
		album.updateImageUrl(albumService.getAlbumImageUrl(album.getImagePath()));
		return new AlbumResponse(album);
	}
}
