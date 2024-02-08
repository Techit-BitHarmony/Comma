package com.bitharmony.comma.album.album.dto;

import com.bitharmony.comma.album.album.entity.Album;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumCreateRequest(@NotEmpty(message = "앨범 이름을 입력해주세요.") @Size(max = 50) String albumname,
								 @NotEmpty(message = "앨범 파일은 필수입니다.") String filePath,
								 String genre,
								 boolean license,
								 String licenseDescription,
								 boolean permit,
								 int price

) {
	public Album toEntity() {
		return Album.builder()
			.albumname(albumname)
			.filePath(filePath)
			.genre(genre)
			.license(license)
			.licenseDescription(licenseDescription)
			.permit(permit)
			.price(price)
			.build();
	}
}