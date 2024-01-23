package com.bitharmony.comma.domain.album.album.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bitharmony.comma.domain.album.album.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
	Optional<Album> findByAlbumname(String albumname);
}
