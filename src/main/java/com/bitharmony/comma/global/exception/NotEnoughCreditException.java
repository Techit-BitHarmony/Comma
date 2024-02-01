package com.bitharmony.comma.global.exception;

public class NotEnoughCreditException extends CommaException {

    private final static String MESSAGE = "잔여 크레딧이 부족합니다."; // 필요한 메시지 삽입.

    public NotEnoughCreditException() { // 생성자
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() { // 메서드 구현
        return 400; // 상황에 맞게 상태 코드 변경
    }
}


/*** 사용법
 ~~~ Exception 과 같이 정의

 throw new MemberNotFoundException();

 아래와 같이 Client로 전달 됨.

 {
 code : 400
 message : "존재하지 않는 유저입니다."
 }
 ***/

