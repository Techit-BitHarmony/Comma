package com.bitharmony.comma.streaming.util;

import com.bitharmony.comma.streaming.service.SseProvider;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class EncodingStatusListener implements MessageListener {

    private final SseProvider sseProvider;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
        String[] parts = messageStr.split(":");

        String key = parts[0];
        String encodeType = parts[1];
        EncodeStatus status = EncodeStatus.valueOf(parts[2]);

        sseProvider.sendEvent(key, encodeType, status);
    }
}
