package com.bitharmony.comma.streaming.service;

import com.bitharmony.comma.global.exception.SseEmitterNotFoundException;
import com.bitharmony.comma.global.exception.SseEmitterSendingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseProvider {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username, Long albumId) {
        SseEmitter emitter = new SseEmitter();
        this.emitters.put(username + ":" + albumId, emitter);
        return emitter;
    }

    public void sendEvent(String key, Object... event) {
        SseEmitter emitter = this.emitters.get(key);

        if (emitter != null) {
            try {
                emitter.send(event);
            } catch (IOException e) {
                throw new SseEmitterSendingException();
            }
        }
    }

    public void complete(String username, Long albumId) {
        String key = username + ":" + albumId;
        SseEmitter emitter = this.emitters.get(key);

        if (emitter == null) {
            throw new SseEmitterNotFoundException();
        }

        emitter.complete();
        this.emitters.remove(key);
    }
}
