package demo.im.system.send;

import java.util.ArrayList;
import java.util.List;

public class SendChain implements Send {

    List<Send> sends = new ArrayList<>();

    public void addSend(Send send) {
        this.sends.add(send);
    }

    public void send(String message, String user) {
        for (Send send : sends) {
            send.send(message, user);
        }
    }

    @Override
    public void sendGroupByUsers(String message, List<String> users) {
        for (Send send : sends) {
            send.sendGroupByUsers(message, users);
        }
    }

    @Override
    public void sendGroupByTag(String message, String tag) {
        for (Send send : sends) {
            send.sendGroupByTag(message, tag);
        }
    }

    @Override
    public void sendGroupByTags(String message, List<String> tags) {
        for (Send send : sends) {
            send.sendGroupByTags(message, tags);
        }
    }

    @Override
    public void sendGroupAll(String message) {
        for (Send send : sends) {
            send.sendGroupAll(message);
        }
    }

}
