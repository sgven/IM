package demo.im.sse.api;

import demo.im.sse.push.IPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class APIController {

    @Autowired
    private IPushService iPushService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(String id) {
        // 直接返回 SseEmitter 对象就可以和客户端连接
        SseEmitter sseEmitter = iPushService.subscribe(id);
        return sseEmitter;
    }

    @RequestMapping("/push")
    public void push(String id) {
        iPushService.push(id, "test");
    }
}
