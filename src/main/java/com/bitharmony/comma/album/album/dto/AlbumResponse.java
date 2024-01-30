package com.bitharmony.comma.album.album.dto;

import lombok.Builder;

@Builder
public record AlbumResponse(String albumname, String genre, boolean license, String licenseDescription, String imgPath,
							String filePath, boolean permit, int price) {
}