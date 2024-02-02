package com.bitharmony.comma.streaming.repository;

import com.bitharmony.comma.streaming.entity.Status;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {
    Optional<Status> findByAlbumId(Long albumId);
}
