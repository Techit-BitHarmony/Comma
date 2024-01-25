package com.bitharmony.comma.streaming.dto;

import jakarta.validation.constraints.NotBlank;

public record UploadUrlRequest(
        @NotBlank(message = "음원을 올려주시길 바랍니다.")
        String filename
) {

}
