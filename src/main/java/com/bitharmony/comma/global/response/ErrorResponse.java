package com.bitharmony.comma.global.response;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validMessages;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validMessages) {
        this.code = code;
        this.message = message;
        this.validMessages = validMessages;
    }
}