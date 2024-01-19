package com.bitharmony.comma.global.exception;


public abstract class CommaException extends RuntimeException {
    public CommaException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
