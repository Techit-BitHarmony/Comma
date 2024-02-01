package com.bitharmony.comma.album.album.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumLikeId implements Serializable {
	private Long album;
	private Long member;
}