package com.bitharmony.comma.album.album.dto;

import lombok.Builder;

@Builder
public record AlbumListResponse(Long id, String albumname,
								String genre,
								String imgPath,
								boolean permit,
								int price,
								String artistNickname,
								String artistUsername) {
}