package demo.im.system.send;

import java.util.List;

/**
 * 在发送渠道的维度上进行扩展
 */
public interface Send {

    /**
     * 发送消息给指定用户
     * @param message
     * @param user
     */
    void send(String message, String user);

    /**
     * 根据用户，群发消息
     * @param message
     * @param users
     */
    void sendGroupByUsers(String message, List<String> users);

    /**
     * 根据标签，群发消息
     * @param message
     * @param tag
     */
    void sendGroupByTag(String message, String tag);

    /**
     * 根据多个标签，群发消息
     * @param message
     * @param tags
     */
    void sendGroupByTags(String message, List<String> tags);

    /**
     * 群发消息给所有人
     * @param message
     */
    void sendGroupAll(String message);
}
