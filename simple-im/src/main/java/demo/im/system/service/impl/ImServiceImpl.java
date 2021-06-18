package demo.im.system.service.impl;

import demo.im.system.send.SendChain;
import demo.im.system.service.ImService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 这里只是简单演示，消息推送
 *
 * 后续的优化扩展：
 * 1.消息持久化存储
 * 2.消息的产生和消费，通过消息队列解耦
 */
@Service
public class ImServiceImpl implements ImService {

    private SendChain sendChain = new SendChain();

    public SendChain getSendChain() {
        return sendChain;
    }

    public void setSendChain(SendChain sendChain) {
        this.sendChain = sendChain;
    }

    @Override
    public void send(String message, String user) {
        sendChain.send(message, user);
    }

    @Override
    public void sendGroupByUsers(String message, List<String> users) {
        sendChain.sendGroupByUsers(message, users);
    }

    @Override
    public void sendGroupByTag(String message, String tag) {
        sendChain.sendGroupByTag(message, tag);
    }

    @Override
    public void sendGroupByTags(String message, List<String> tags) {
        sendChain.sendGroupByTags(message, tags);
    }

    @Override
    public void sendGroupAll(String message) {
        sendChain.sendGroupAll(message);
    }
}
