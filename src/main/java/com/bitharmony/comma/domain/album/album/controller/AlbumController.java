package com.bitharmony.comma.domain.album.album.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitharmony.comma.domain.album.album.service.AlbumService;

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

	// @GetMapping(value = "/create")
	// public String create(@RequestBody @Valid AlbumCreateRequest request) {
	// 	albumService.create(request);
	// 	return "redirect:/";
	// }

	// @PostMapping("/uploadAlbumImg")
	// public String uploadAlbumImg(@ModelAttribute )
}
