package com.bitharmony.comma.global.exception;

public class CreditShortageException extends CommaException {
    public CreditShortageException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
