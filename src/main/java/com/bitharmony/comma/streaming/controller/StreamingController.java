package com.bitharmony.comma.streaming.controller;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.service.AlbumService;
import com.bitharmony.comma.streaming.dto.EncodeStatusRequest;
import com.bitharmony.comma.streaming.dto.EncodeStatusResponse;
import com.bitharmony.comma.streaming.dto.UploadUrlRequest;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.service.StreamingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final AlbumService albumService;
    private final StreamingService streamingService;

    @GetMapping("/upload/presigned-url") // get upload presigned url
    public UploadUrlResponse getUploadURL(@ModelAttribute @Valid UploadUrlRequest uploadUrlRequest) {
        return streamingService.generateURL(uploadUrlRequest.filename());
    }

    @PostMapping("/encode/status") // callback encode status
    public void encodeStatus(@RequestBody EncodeStatusRequest encodeStatusRequest) {
        Album album = albumService.getAlbumByFilePath(encodeStatusRequest.filePath());
        streamingService.encodeStatus(album, encodeStatusRequest.status());
    }

    @GetMapping("/encode/status/{albumId}")
    public EncodeStatusResponse getEncodeStatus(@PathVariable("albumId") Long albumId) {
        return EncodeStatusResponse.from(streamingService.getStatus(albumId));
    }

}
