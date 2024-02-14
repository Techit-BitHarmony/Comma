package com.bitharmony.comma.global.exception.credit;

import com.bitharmony.comma.global.exception.CommaException;

public class CreditLogNotFoundException extends CommaException {

    private final static String MESSAGE = "존재하지 않는 크레딧 내역입니다."; // 필요한 메시지 삽입.

    public CreditLogNotFoundException() { // 생성자
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() { // 메서드 구현
        return 400; // 상황에 맞게 상태 코드 변경
    }
}
