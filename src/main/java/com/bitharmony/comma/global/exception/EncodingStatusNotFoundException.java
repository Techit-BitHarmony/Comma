package com.bitharmony.comma.global.exception;

public class EncodingStatusNotFoundException extends CommaException{
    private final static String MESSAGE = "해당 파일을 찾을 수 없습니다.";

    public EncodingStatusNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}