package demo.im.system.send;

import demo.im.system.access.MyWebSocketHandler;

import java.util.List;

public class AppMsgSend implements Send {

    private MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler();

    @Override
    public void send(String message, String user) {
        myWebSocketHandler.send(message, user);
    }

    @Override
    public void sendGroupByUsers(String message, List<String> users) {
        myWebSocketHandler.sendGroupByUsers(message, users);
    }

    @Override
    public void sendGroupByTag(String message, String tag) {
        myWebSocketHandler.sendGroupByTag(message, tag);
    }

    @Override
    public void sendGroupByTags(String message, List<String> tags) {
        myWebSocketHandler.sendGroupByTags(message, tags);
    }

    @Override
    public void sendGroupAll(String message) {
        myWebSocketHandler.sendGroupAll(message);
    }
}
