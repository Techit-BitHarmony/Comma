package com.bitharmony.comma.streaming.dto;

import java.net.URL;
import lombok.Builder;

@Builder
public record UploadUrlResponse (URL uploadUrl, String filePath) {

    public static UploadUrlResponse from(URL url, String filePath) {
        return UploadUrlResponse.builder()
                .uploadUrl(url)
                .filePath(filePath)
                .build();
    }

}
