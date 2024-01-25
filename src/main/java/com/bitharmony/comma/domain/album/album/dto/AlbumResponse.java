package com.bitharmony.comma.domain.album.album.dto;

import lombok.Builder;

@Builder
public record AlbumResponse(String albumname, String genre, boolean license, String licenseDescription, String imgPath,
							boolean permit, int price) {
}