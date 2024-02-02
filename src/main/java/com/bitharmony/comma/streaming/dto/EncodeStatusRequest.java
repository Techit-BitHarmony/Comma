package com.bitharmony.comma.streaming.dto;

import com.bitharmony.comma.streaming.util.EncodeStatus;

public record EncodeStatusRequest(
        int categoryId,
        String categoryName,
        int encodingOptionId,
        int fileId,
        String filePath,
        String outputType,
        EncodeStatus status
) {

}
