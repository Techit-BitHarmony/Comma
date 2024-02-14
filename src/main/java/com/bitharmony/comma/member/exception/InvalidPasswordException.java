package com.bitharmony.comma.member.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class InvalidPasswordException extends CommaException {
    private final static String MESSAGE = "입력한 두개의 비밀번호가 일치하지 않습니다.";

    public InvalidPasswordException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
