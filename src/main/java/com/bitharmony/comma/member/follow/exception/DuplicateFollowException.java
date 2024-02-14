package com.bitharmony.comma.member.follow.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class DuplicateFollowException extends CommaException {
    private final static String MESSAGE = "이미 팔로우한 상태입니다.";

    public DuplicateFollowException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
