package com.bitharmony.comma.global.exception.streaming;

import com.bitharmony.comma.global.exception.CommaException;

public class EncodingStatusNotFoundException extends CommaException {
    private final static String MESSAGE = "파일의 상태를 확인 할 수 없습니다.";

    public EncodingStatusNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}