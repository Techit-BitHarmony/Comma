package com.bitharmony.comma.domain.album.album.dto;

import com.bitharmony.comma.domain.album.album.entity.Album;

import lombok.Builder;

@Builder
public record AlbumResponse(String albumname, String genre, boolean license, String licenseDescription, String imgPath,
							String filePath, boolean permit, int price) {
	public AlbumResponse(Album album) {
		this(album.getAlbumname(), album.getGenre(), album.isLicense(), album.getLicenseDescription(),
			album.getImagePath(), album.getFilePath(), album.isPermit(), album.getPrice());
	}
}