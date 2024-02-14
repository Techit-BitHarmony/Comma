package com.bitharmony.comma.member.follow.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class SelfFollowException extends CommaException {
    private final static String MESSAGE = "본인을 팔로우 할 수 없습니다.";

    public SelfFollowException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
