package com.bitharmony.comma.album.album.exception;

import com.bitharmony.comma.global.exception.CommaException;

public class AlbumPermissionException extends CommaException {
	private final static String MESSAGE = "권한이 없습니다.";

	public AlbumPermissionException() { // 생성자
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
