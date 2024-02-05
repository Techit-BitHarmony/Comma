package com.bitharmony.comma.global.exception;

public class WithdrawNotFoundException extends CommaException {

    private final static String MESSAGE = "출금 신청 내역이 없습니다."; // 필요한 메시지 삽입.

    public WithdrawNotFoundException() { // 생성자
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() { // 메서드 구현
        return 400; // 상황에 맞게 상태 코드 변경
    }
}