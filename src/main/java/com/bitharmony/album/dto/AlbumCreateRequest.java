package com.bitharmony.album.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumCreateRequest(
	@NotEmpty @Size(max = 50) String albumname,
	String genre,
	boolean license,
	String licenseDescription,
	@NotEmpty String filePath,
	String imagePath,
	boolean permit,
	int price
) {}