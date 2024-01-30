package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.streaming.util.NcpMusicUtil;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamingService {

    private final NcpMusicUtil ncpMusicUtil;

    public URL generateURL(String fileName) {
        String objectName = UUID.randomUUID() + getExtension(fileName);
        return ncpMusicUtil.generatePresignedUrl(objectName);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}
