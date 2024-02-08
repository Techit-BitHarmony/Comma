package com.bitharmony.comma.member.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class IncorrectPasswordException extends CommaException {
    private final static String MESSAGE = "입력한 비밀번호와 일치하지 않습니다.";

    public IncorrectPasswordException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
