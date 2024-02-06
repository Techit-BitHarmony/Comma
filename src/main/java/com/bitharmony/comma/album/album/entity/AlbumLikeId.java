package com.bitharmony.comma.album.album.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.io.Serializable;

import com.bitharmony.comma.member.entity.Member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@Getter
public class AlbumLikeId implements Serializable {
	@ManyToOne(fetch = LAZY)
	private Album album;
	@ManyToOne(fetch = LAZY)
	private Member member;
}