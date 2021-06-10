package demo.im.sse.push;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IPushService {
    SseEmitter subscribe(String identify);
    void push(String identify, Object message);
}
