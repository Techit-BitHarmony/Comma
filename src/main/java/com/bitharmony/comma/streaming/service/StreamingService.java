package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.global.exception.streaming.EncodingFailureException;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.util.EncodeStatus;
import com.bitharmony.comma.streaming.util.EncodingStatusListener;
import com.bitharmony.comma.streaming.util.NcpMusicUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer container;
    private final EncodingStatusListener encodingStatusListener;
    private final ChannelTopic topicStatus;

    private final NcpMusicUtil ncpMusicUtil;

    static private final String CHANNEL_NAME = "encodingStatus";

    public UploadUrlResponse generateURL(String fileName) {
        String filePath = ncpMusicUtil.path + UUID.randomUUID() + getExtension(fileName);
        return UploadUrlResponse.from(ncpMusicUtil.generatePresignedUrl(filePath), filePath);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public void encodeStatus(String username, Long albumId, String outputType, EncodeStatus status) {
        switch (status) {
            case WAITING -> {
                container.addMessageListener(encodingStatusListener, topicStatus); // 토픽 등록
                sendEncodingStatus(username, albumId, outputType, EncodeStatus.WAITING);
            }
            case RUNNING -> {
                sendEncodingStatus(username, albumId, outputType, EncodeStatus.RUNNING);
            }
            case COMPLETE -> {
                sendEncodingStatus(username, albumId, outputType, EncodeStatus.COMPLETE);
                container.removeMessageListener(encodingStatusListener, topicStatus); // 토픽 해제
            }
            case FAILURE -> {
                sendEncodingStatus(username, albumId, outputType, EncodeStatus.FAILURE);
                throw new EncodingFailureException();
            }
            case CANCELED -> {
                sendEncodingStatus(username, albumId, outputType, EncodeStatus.CANCELED);
            }
        }
    }

    private void sendEncodingStatus(String username, Long albumId, String outputType, EncodeStatus status) {
        String message = username + ":" + albumId + ":" + outputType;
        redisTemplate.convertAndSend(CHANNEL_NAME, message + ":" + status);
    }

}
