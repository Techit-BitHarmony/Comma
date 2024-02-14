package com.bitharmony.comma.album.album.exception;

import com.bitharmony.comma.global.exception.CommaException;

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
