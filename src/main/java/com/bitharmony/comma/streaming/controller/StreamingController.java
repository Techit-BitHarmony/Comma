package com.bitharmony.comma.streaming.controller;

import com.bitharmony.comma.album.album.entity.Album;
import com.bitharmony.comma.album.album.service.AlbumService;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.streaming.dto.EncodeStatusRequest;
import com.bitharmony.comma.streaming.dto.UploadUrlRequest;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.service.SseProvider;
import com.bitharmony.comma.streaming.service.StreamingService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final AlbumService albumService;
    private final StreamingService streamingService;
    private final SseProvider sseProvider;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload") // get upload presigned url
    public GlobalResponse<UploadUrlResponse> getUploadURL(@ModelAttribute @Valid UploadUrlRequest uploadUrlRequest) {
        return GlobalResponse.of("200", streamingService.generateURL(uploadUrlRequest.filename()));
    }

    @PostMapping("/status") // callback encode status
    public void encodeStatus(@RequestBody EncodeStatusRequest encodeStatusRequest) {
        streamingService.encodeStatus(encodeStatusRequest.filePath(),
                encodeStatusRequest.outputType(), encodeStatusRequest.status());
    }

    @GetMapping("/status") // sse emitter subscribe
    public SseEmitter getEncodeStatus(@RequestParam(name = "filePath") String filePath) {
        return sseProvider.subscribe(filePath);
    }

}
