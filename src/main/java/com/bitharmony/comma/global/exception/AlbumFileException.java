package com.bitharmony.comma.global.exception;

public class AlbumFileException extends CommaException {
	private final static String MESSAGE = "앨범 파일이 문제가 있습니다.";

	public AlbumFileException() { // 생성자
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 422;
	}
	//파일 업로드 실패는 클라이언트가 제공한 파일이 서버에서 처리할 수 없는 경우에 발생하므로,
	//이 경우에는 422(Unprocessable Entity) 상태 코드를 사용
}

/*** 사용법
 ~~~ Exception 과 같이 정의

 throw new AlbumFileException();

 아래와 같이 Client로 전달 됨.

 {
 code : 422
 message : "앨범 파일이 문제가 있습니다."
 }
 ***/