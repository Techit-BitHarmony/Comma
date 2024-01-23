package com.bitharmony.comma.domain.album.album.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;

@Builder
public record AlbumImageUploadResponse(MultipartFile imageFile) {
}
