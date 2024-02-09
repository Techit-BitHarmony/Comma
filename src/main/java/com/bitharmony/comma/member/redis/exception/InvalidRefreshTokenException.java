package com.bitharmony.comma.member.redis.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class InvalidRefreshTokenException extends CommaException {
    private final static String MESSAGE = "Refresh token이 유효하지 않습니다.";

    public InvalidRefreshTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
