package com.bitharmony.comma.streaming.dto;

import java.net.URL;
import lombok.Builder;

@Builder
public record UploadUrlResponse (URL uploadUrl) {

    public static UploadUrlResponse from(URL url) {
        return UploadUrlResponse.builder()
                .uploadUrl(url)
                .build();
    }

}
