package com.bitharmony.comma.global.exception.streaming;

import com.bitharmony.comma.global.exception.CommaException;

public class SseEmitterNotFoundException extends CommaException {

    private final static String MESSAGE = "찾으시는 구독 정보가 없습니다.";

    public SseEmitterNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}