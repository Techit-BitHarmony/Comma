package com.bitharmony.comma.global.exception;

public class MemberDuplicateException extends CommaException{
    private final static String MESSAGE = "이미 존재하는 유저가 있습니다.";

    public MemberDuplicateException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
