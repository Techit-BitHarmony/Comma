package com.bitharmony.album.dto;

import com.bitharmony.album.entity.Album;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumCreateRequest(@NotEmpty @Size(max = 50) String albumname, String genre, boolean license,
								 String licenseDescription, @NotEmpty String filePath, String imagePath, boolean permit,
								 int price

) {
	public Album toEntity() {
		return Album.builder()
			.albumname(albumname)
			.genre(genre)
			.license(license)
			.licenseDescription(licenseDescription)
			.filePath(filePath)
			.imagePath(imagePath)
			.permit(permit)
			.price(price)
			.build();
	}
}