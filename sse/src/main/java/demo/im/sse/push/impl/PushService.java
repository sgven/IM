package demo.im.sse.push.impl;

import demo.im.sse.push.IPushService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class PushService implements IPushService {

    private static final Map<String, SseEmitter> connectionMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(String identify) {
        // 设置超时时间为5小时
        long millis = TimeUnit.SECONDS.toMillis(60 * 60 * 5);
        SseEmitter sseEmitter = new SseEmitter(millis);
        connectionMap.put(identify, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void push(String identify, Object message) {
        SseEmitter sseEmitter = connectionMap.get(identify);
        if (Objects.nonNull(sseEmitter)) {
            try {
                sseEmitter.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //log
        }
    }
}
