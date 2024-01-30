package com.bitharmony.comma.album.file.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
	IMAGE("image"),
	AUDIO("audio");

	private final String type;
}