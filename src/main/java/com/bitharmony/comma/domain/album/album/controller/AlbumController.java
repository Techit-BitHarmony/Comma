package com.bitharmony.comma.domain.album.album.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.domain.album.album.dto.AlbumResponse;
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
		@RequestParam("musicImageFile") MultipartFile multipartFile) {

		AlbumResponse albumResponse = albumService.release(request, multipartFile);
		return new ResponseEntity<>(albumResponse, HttpStatus.CREATED);
	}

	// @GetMapping("/{id}")
	// public ResponseEntity<Album> getAlbum(
	// 	@PathVariable long id
	// ) {
	// 	Album album = albumService.getAlbumById(id).orElseThrow(RuntimeException::new);
	// }
	//
	// @PutMapping("/{id}")
	// public ResponseEntity<Album> editAlbum(@PathVariable long id, @Valid AlbumEditRequest request,
	// 	@RequestParam("musicImageFile") MultipartFile multipartFile) {
	//
	// 	Album album = albumService.getAlbumById(id).orElseThrow(RuntimeException::new);
	// 	//Album editedAlbum = albumService.edit(request, multipartFile, album);
	// 	return new ResponseEntity<>(editedAlbum, HttpStatus.OK);
	// }
}
