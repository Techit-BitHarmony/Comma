package com.bitharmony.comma.album.album.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
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
import com.bitharmony.comma.album.album.dto.AlbumListResponse;
import com.bitharmony.comma.album.album.dto.AlbumResponse;
import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.exception.AlbumFieldException;
import com.bitharmony.comma.album.album.exception.AlbumPermissionException;
import com.bitharmony.comma.album.album.service.AlbumLikeService;
import com.bitharmony.comma.album.album.service.AlbumService;
import com.bitharmony.comma.global.exception.MemberNotFoundException;
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
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile, Principal principal) {

		Member member = memberService.getMemberByUsername(principal.getName());

		if (!albumService.canRelease(request.albumname(), musicImageFile, member)) {
			throw new AlbumFieldException();
		}

		Album album = albumService.release(request, member, musicImageFile);
		return GlobalResponse.of("200", albumToResponseDto(album));
	}

	@GetMapping("/detail/{id}")
	public GlobalResponse getAlbum(@PathVariable long id) {
		Album album = albumService.getAlbumById(id);

		return GlobalResponse.of("200", albumToResponseDto(album));
	}

	@GetMapping("/{username}")
	public GlobalResponse getUserAlbumList(@PathVariable String username) {
		Member member = memberService.getMemberByUsername(username);

		if (member == null) {
			throw new MemberNotFoundException("존재하지 않는 회원입니다.");
		}

		 Page<AlbumListResponse> albumPage = albumService.getLatest20Albums(username);
		return GlobalResponse.of("200", albumPage);
	}

	@GetMapping("/list")
	public GlobalResponse getAlbumList() {
		Page<AlbumListResponse> albumPage = albumService.getLatest20Albums(null);
		return GlobalResponse.of("200", albumPage);
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public GlobalResponse editAlbum(@PathVariable long id, @Valid AlbumEditRequest request,
		@RequestParam(value = "musicImageFile", required = false) MultipartFile musicImageFile, Principal principal) {
		Album album = albumService.getAlbumById(id);
		Member member = memberService.getMemberByUsername(principal.getName());

		if (!albumService.canEdit(album, principal, musicImageFile, request, member)) {
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

		albumLikeService.like(member, album);
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

		albumLikeService.like(member, album);
		return GlobalResponse.of("200");
	}

	private AlbumResponse albumToResponseDto(Album album) {
		album = album.toBuilder()
			//.filePath(albumService.getAlbumFileUrl(album.getFilePath()))
			.imagePath(albumService.getAlbumImageUrl(album.getImagePath()))
			.build();

		return AlbumResponse.builder()
			.id(album.getId())
			.albumname(album.getAlbumname())
			.genre(album.getGenre())
			.license(album.isLicense())
			.licenseDescription(album.getLicenseDescription())
			.imgPath(album.getImagePath())
			.filePath(album.getFilePath())
			.permit(album.isPermit())
			.price(album.getPrice())
			.artistNickname(album.getMember().getNickname())
			.artistUsername(album.getMember().getUsername())
			.build();
	}
}