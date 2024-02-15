package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.global.exception.streaming.SseEmitterNotFoundException;
import com.bitharmony.comma.global.exception.streaming.SseEmitterSendingException;
import com.bitharmony.comma.streaming.util.EncodeStatus;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseProvider {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private SseEmitter getEmitter(String key) {
        SseEmitter emitter = this.emitters.get(key);

        if (emitter == null) {
            throw new SseEmitterNotFoundException();
        }

        return emitter;
    }


    public SseEmitter subscribe(String username) {
        SseEmitter emitter = new SseEmitter((long) (60000 * 10));
        this.emitters.put(username, emitter);
        return emitter;
    }

    public void sendEvent(String key, Object... event) {
        SseEmitter emitter = getEmitter(key);

        try {
            emitter.send(SseEmitter.event().reconnectTime(30000)
                    .data(event, MediaType.APPLICATION_JSON)
                    .id(UUID.randomUUID().toString())
                    .name("Encoding Status")
            );
        } catch (IOException e) {
            throw new SseEmitterSendingException();
        }

        if (isComplete(event)) {
            complete(key);
        }
    }

    public void complete(String key) {
        SseEmitter emitter = getEmitter(key);

        emitter.complete();
        this.emitters.remove(key);
    }

    private boolean isComplete(Object... events) {
        for (Object event : events) {
            if (event == EncodeStatus.COMPLETE) {
                return true;
            }
        }

        return false;
    }
}
