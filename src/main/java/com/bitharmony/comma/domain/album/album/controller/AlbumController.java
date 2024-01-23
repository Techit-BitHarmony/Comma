package com.bitharmony.comma.domain.album.album.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.domain.album.album.dto.AlbumCreateRequest;
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
	public String releaseAlbum(@Valid AlbumCreateRequest request,
		@RequestParam("musicImageFile") MultipartFile musicImageFile) {
		albumService.release(request,musicImageFile);
		return "domain/album/album_detail";
	}

	// @PostMapping("/uploadAlbumImg")
	// public String uploadAlbumImg(@ModelAttribute )
}
