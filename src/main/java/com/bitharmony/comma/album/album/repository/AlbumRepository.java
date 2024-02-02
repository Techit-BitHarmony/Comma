package com.bitharmony.comma.album.album.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitharmony.comma.album.album.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
	Optional<Album> findByAlbumname(String albumname);
	Optional<Album> findByFilePath(String filePath);
}