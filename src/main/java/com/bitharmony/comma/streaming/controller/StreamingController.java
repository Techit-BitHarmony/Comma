package com.bitharmony.comma.streaming.controller;

import com.bitharmony.comma.streaming.dto.UploadUrlRequest;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.service.StreamingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final StreamingService streamingService;

    @GetMapping("/upload")
    public UploadUrlResponse getUploadURL(@ModelAttribute @Valid UploadUrlRequest uploadUrlRequest) {
        return UploadUrlResponse.from(streamingService.generateURL(uploadUrlRequest.filename()));
    }

}
