package com.bitharmony.comma.global.exception.Donation;

import com.bitharmony.comma.global.exception.CommaException;

public class QuartzJobNotDeletedException extends CommaException {

    private static String MESSAGE = "등록된 작업을 삭제하는데 실패했습니다.";

    public QuartzJobNotDeletedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
