package com.bitharmony.comma.global.exception;

public class SseEmitterSendingException extends CommaException {

    private final static String MESSAGE = "메시지 전송에 실패했습니다.";

    public SseEmitterSendingException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}