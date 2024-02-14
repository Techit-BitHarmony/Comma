package com.bitharmony.comma.album.album.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AlbumEditRequest(@NotEmpty(message = "앨범 이름을 입력해주세요.") @Size(max = 50) String albumname,
							   String genre,
							   boolean license,
							   String licenseDescription,
							   boolean permit,
							   int price) {
}