package com.bitharmony.album.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bitharmony.album.dto.AlbumCreateRequest;
import com.bitharmony.album.service.AlbumService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller("/album")
@RequiredArgsConstructor
public class AlbumController {

	private final AlbumService albumService;

	/**
	 * 앨범 등록
	 */
	@GetMapping(value = "/create")
	public String Create(@RequestBody @Valid AlbumCreateRequest request) {
		//서비스
		return "album/create";
	}
}
