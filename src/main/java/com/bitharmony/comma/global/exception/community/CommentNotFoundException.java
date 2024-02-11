package com.bitharmony.comma.global.exception.community;


import com.bitharmony.comma.global.exception.CommaException;

public class CommentNotFoundException extends CommaException {

    private final static String MESSAGE = "존재하지 않는 댓글입니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
