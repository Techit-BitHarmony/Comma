package com.bitharmony.comma.global.exception;

public class MemberNotFoundException extends CommaException {

    private final static String MESSAGE = "존재하지 않는 유저입니다."; // 필요한 메시지 삽입.

    public MemberNotFoundException(String message) { // 생성자
        super(message);
    }

    @Override
    public int getStatusCode() { // 메서드 구현
        return 400; // 상황에 맞게 상태 코드 변경
    }
}