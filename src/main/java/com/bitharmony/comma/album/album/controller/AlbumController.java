package com.bitharmony.comma.album.album.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bitharmony.comma.album.album.dto.AlbumCreateRequest;
import com.bitharmony.comma.album.album.dto.AlbumEditRequest;
import com.bitharmony.comma.album.album.dto.AlbumResponse;
import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.service.AlbumLikeService;
import com.bitharmony.comma.album.album.service.AlbumService;
import com.bitharmony.comma.global.exception.AlbumFieldException;
import com.bitharmony.comma.global.exception.AlbumPermissionException;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {

	private final AlbumService albumService;
	private final AlbumLikeService albumLikeService;
	private final MemberService memberService;

	@PostMapping("/release")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse releaseAlbum(@Valid AlbumCreateRequest request,
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile,
		Principal principal) {

		Member member = memberService.getMemberByUsername(principal.getName());

		if (!albumService.canRelease(request.albumname(), musicImageFile, member)) {
			throw new AlbumFieldException();
		}

		Album album = albumService.release(request, member, musicImageFile);
		return GlobalResponse.of("200", albumToResponseDto(album));
	}

	@GetMapping("/{id}")
	public GlobalResponse getAlbum(@PathVariable long id) {
		Album album = albumService.getAlbumById(id);

		return GlobalResponse.of("200", albumToResponseDto(album));
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse editAlbum(@PathVariable long id,@Valid AlbumEditRequest request,
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile,
		Principal principal) {
		Album album = albumService.getAlbumById(id);
		Member member = memberService.getMemberByUsername(principal.getName());

		if (!albumService.canEdit(album, principal, member)) {
			throw new AlbumFieldException();
		}

		Album editedAlbum = albumService.edit(request, album, musicImageFile);
		return GlobalResponse.of("200", albumToResponseDto(editedAlbum));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse deleteAlbum(@PathVariable long id, Principal principal) {
		Album album = albumService.getAlbumById(id);

		if (!albumService.canDelete(album, principal)) {
			throw new AlbumPermissionException();
		}

		albumService.delete(album);
		return GlobalResponse.of("200");
	}

	@PostMapping(value = "/{albumId}/like")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse like(@PathVariable long albumId, Principal principal) {
		Member member = memberService.getMemberByUsername(principal.getName());
		Album album = albumService.getAlbumById(albumId);

		if (!albumLikeService.canLike(member, album)) {
			throw new AlbumPermissionException();
		}

		albumLikeService.like(member,album);
		return GlobalResponse.of("200");
	}

	@PostMapping(value = "/{albumId}/cancelLike")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse cancelLike(@PathVariable long albumId, Principal principal) {
		Member member = memberService.getMemberByUsername(principal.getName());
		Album album = albumService.getAlbumById(albumId);

		if (!albumLikeService.canCancelLike(member, album)) {
			throw new AlbumPermissionException();
		}

		albumLikeService.like(member,album);
		return GlobalResponse.of("200");
	}

	private AlbumResponse albumToResponseDto(Album album) {
		album = album.toBuilder()
			.filePath(albumService.getAlbumFileUrl(album.getFilePath()))
			.imagePath(albumService.getAlbumImageUrl(album.getImagePath()))
			.build();

		return AlbumResponse.builder()
			.albumname(album.getAlbumname())
			.genre(album.getGenre())
			.license(album.isLicense())
			.licenseDescription(album.getLicenseDescription())
			.imgPath(album.getImagePath())
			.filePath(album.getFilePath())
			.permit(album.isPermit())
			.price(album.getPrice())
			.build();
	}
}