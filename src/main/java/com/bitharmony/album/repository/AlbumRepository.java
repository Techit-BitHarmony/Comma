package com.bitharmony.album.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bitharmony.album.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
	Optional<Album> findByAlbumname(String albumname);
}
