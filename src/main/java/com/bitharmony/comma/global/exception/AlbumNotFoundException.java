package com.bitharmony.comma.global.exception;

public class AlbumNotFoundException extends CommaException {
	private final static String MESSAGE = "앨범을 찾을 수 없습니다.";

	public AlbumNotFoundException() { // 생성자
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}

/*** 사용법
 ~~~ Exception 과 같이 정의

 throw new AlbumNotFoundException();

 아래와 같이 Client로 전달 됨.

 {
 code : 400
 message : "앨범을 찾을 수 없습니다."
 }
 ***/