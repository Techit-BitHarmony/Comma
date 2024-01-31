package com.bitharmony.comma.global.exception;

public class AlbumNotFoundException extends CommaException {
	private final static String MESSAGE = "존재하지 않는 앨범입니다.";

	public AlbumNotFoundException(String message) {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
