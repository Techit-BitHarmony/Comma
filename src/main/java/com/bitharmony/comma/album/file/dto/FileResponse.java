package com.bitharmony.comma.album.file.dto;

import lombok.Builder;

@Builder
public record FileResponse(String originalFileName, String uploadFileName,
						   String uploadFileUrl) {
}
