package com.bitharmony.comma.domain.album.album.dto;

import com.bitharmony.comma.domain.album.album.entity.Album;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumEditRequest(@NotEmpty @Size(max = 50) String albumname, String genre, boolean license,
							   String licenseDescription, @NotEmpty String filePath, boolean permit, int price) {
	public Album toEntity() {
		return Album.builder()
			.albumname(albumname)
			.genre(genre)
			.license(license)
			.licenseDescription(licenseDescription)
			.filePath(filePath)
			.permit(permit)
			.price(price)
			.build();
	}
}