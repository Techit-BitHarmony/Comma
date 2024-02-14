package com.bitharmony.comma.member.follow.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class FollowNotFoundException extends CommaException {
    private final static String MESSAGE = "팔로우 정보가 없습니다.";

    public FollowNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
