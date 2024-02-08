package com.bitharmony.comma.global.exception.Donation;

import com.bitharmony.comma.global.exception.CommaException;

public class DonationListNotFoundException extends CommaException {

    private final static String MESSAGE = "후원 내역이 존재하지 않습니다.";

    public DonationListNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
