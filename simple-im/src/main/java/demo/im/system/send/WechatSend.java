package demo.im.system.send;

import java.util.List;

public class WechatSend implements Send {
    @Override
    public void send(String message, String to) {
        System.out.println("WechatSend:" + message + ",to:" + to);
    }

    @Override
    public void sendGroupByUsers(String message, List<String> users) {

    }

    @Override
    public void sendGroupByTag(String message, String tag) {

    }

    @Override
    public void sendGroupByTags(String message, List<String> tags) {

    }

    @Override
    public void sendGroupAll(String message) {

    }
}
