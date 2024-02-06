package com.bitharmony.comma.global.exception;

public class EncodingFailureException extends CommaException{
    private final static String MESSAGE = "음원 인코딩에 실패하였습니다.";

    public EncodingFailureException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 502;
    }
}