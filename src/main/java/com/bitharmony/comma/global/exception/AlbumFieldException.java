package com.bitharmony.comma.global.exception;

public class AlbumFieldException extends CommaException {
	private final static String MESSAGE = "앨범 필드가 잘못되었습니다.";

	public AlbumFieldException() { // 생성자
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}

/*** 사용법
 ~~~ Exception 과 같이 정의

 throw new AlbumFieldException();

 아래와 같이 Client로 전달 됨.

 {
 code : 400
 message : "앨범 필드가 잘못되었습니다."
 }
 ***/