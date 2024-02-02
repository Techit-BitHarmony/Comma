package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.global.exception.EncodingStatusNotFoundException;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.entity.Status;
import com.bitharmony.comma.streaming.repository.StatusRepository;
import com.bitharmony.comma.streaming.util.EncodeStatus;
import com.bitharmony.comma.streaming.util.NcpMusicUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamingService {

    private final StatusRepository statusRepository;
    private final NcpMusicUtil ncpMusicUtil;

    public UploadUrlResponse generateURL(String fileName) {
        String filePath = ncpMusicUtil.path + UUID.randomUUID() + getExtension(fileName);
        return UploadUrlResponse.from(ncpMusicUtil.generatePresignedUrl(filePath), filePath);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public Status getStatus(Long albumId) {
        return statusRepository.findByAlbumId(albumId)
                .orElseThrow(EncodingStatusNotFoundException::new);
    }

    public void encodeStatus(Album album, EncodeStatus status) {
        switch (status) {
            case WAITING -> {
                updateEncodeStatus(album.getId(), EncodeStatus.WAITING);
            }
            case RUNNING -> {
                updateEncodeStatus(album.getId(), EncodeStatus.RUNNING);
            }
            case COMPLETE -> {
                updateEncodeStatus(album.getId(), EncodeStatus.COMPLETE);
            }
            case FAILURE -> {
                updateEncodeStatus(album.getId(), EncodeStatus.FAILURE);
                throw new EncodingStatusNotFoundException();
            }
            case CANCELED -> {
                updateEncodeStatus(album.getId(), EncodeStatus.CANCELED);
            }
        }
    }

    private void updateEncodeStatus(Long albumId, EncodeStatus status) {
        statusRepository.save(Status.builder()
               .albumId(albumId)
               .encodeStatus(status)
               .build()
        );
    }

}
