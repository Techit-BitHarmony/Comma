package com.bitharmony.comma.global.exception.credit;

import com.bitharmony.comma.global.exception.CommaException;

public class HandledWithdrawException extends CommaException {

    private final static String MESSAGE = "이미 처리된 출금 신청 입니다."; // 필요한 메시지 삽입.

    public HandledWithdrawException() { // 생성자
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() { // 메서드 구현
        return 400; // 상황에 맞게 상태 코드 변경
    }
}