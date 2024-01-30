package com.bitharmony.comma.album.album.dto;

import com.bitharmony.comma.album.album.entity.Album;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumCreateRequest(@NotEmpty @Size(max = 50) String albumname, String genre, boolean license,
								 String licenseDescription, boolean permit,
								 int price

) {
	public Album toEntity() {
		return Album.builder()
			.albumname(albumname)
			.genre(genre)
			.license(license)
			.licenseDescription(licenseDescription)
			.permit(permit)
			.price(price)
			.build();
	}
}