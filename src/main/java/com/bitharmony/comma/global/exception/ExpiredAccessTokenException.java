package com.bitharmony.comma.global.exception;

public class ExpiredAccessTokenException extends CommaException {
    private final static String MESSAGE = "access token의 유효기간이 만료되었습니다.";

    public ExpiredAccessTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
