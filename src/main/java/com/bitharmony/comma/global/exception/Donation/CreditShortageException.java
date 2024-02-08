package com.bitharmony.comma.global.exception.Donation;

import com.bitharmony.comma.global.exception.CommaException;

public class CreditShortageException extends CommaException {

    private final static String MESSAGE = "크레딧 잔액이 부족합니다.";

    public CreditShortageException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
