package com.bitharmony.comma.member.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class DuplicateNicknameException extends CommaException {
    private final static String MESSAGE = "이미 존재하는 닉네임입니다.";

    public DuplicateNicknameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
