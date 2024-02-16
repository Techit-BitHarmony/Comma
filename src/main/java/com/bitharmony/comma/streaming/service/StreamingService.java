package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.global.exception.streaming.EncodingFailureException;
import com.bitharmony.comma.global.exception.streaming.EncodingStatusNotFoundException;
import com.bitharmony.comma.streaming.dto.UploadUrlResponse;
import com.bitharmony.comma.streaming.util.EncodeStatus;
import com.bitharmony.comma.streaming.util.EncodingStatusListener;
import com.bitharmony.comma.streaming.util.NcpMusicUtil;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public void encodeStatus(String filePath, String outputType, EncodeStatus status) {
        switch (status) {
            case WAITING -> {
                container.addMessageListener(encodingStatusListener, topicStatus); // 토픽 등록
                sendEncodingStatus(extractUUID(filePath), outputType, EncodeStatus.WAITING);
            }
            case RUNNING -> {
                sendEncodingStatus(extractUUID(filePath), outputType, EncodeStatus.RUNNING);
            }
            case COMPLETE -> {
                sendEncodingStatus(extractUUID(filePath), outputType, EncodeStatus.COMPLETE);
                container.removeMessageListener(encodingStatusListener, topicStatus); // 토픽 해제
            }
            case FAILURE -> {
                sendEncodingStatus(extractUUID(filePath), outputType, EncodeStatus.FAILURE);
                throw new EncodingFailureException();
            }
            case CANCELED -> {
                sendEncodingStatus(extractUUID(filePath), outputType, EncodeStatus.CANCELED);
            }
        }
    }

    private void sendEncodingStatus(String filePath, String outputType, EncodeStatus status) {
        String message = extractUUID(filePath) + ":" + outputType;
        redisTemplate.convertAndSend(CHANNEL_NAME, message + ":" + status);
    }

    public String extractUUID(String filePath) {
        String regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filePath);
        if (matcher.find()) {
            return matcher.group();
        }

        throw new EncodingStatusNotFoundException();
    }

}
