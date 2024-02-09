package com.bitharmony.comma.global.exception.streaming;

import com.bitharmony.comma.global.exception.CommaException;

public class MusicFileNotFoundException extends CommaException {
    private final static String MESSAGE = "해당 파일을 찾을 수 없습니다.";

    public MusicFileNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}