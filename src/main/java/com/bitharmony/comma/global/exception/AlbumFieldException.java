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
