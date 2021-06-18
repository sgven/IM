package demo.im.system.controller;

import demo.im.system.send.AppMsgSend;
import demo.im.system.send.SendChain;
import demo.im.system.service.impl.ImServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试IM发消息Controller
 */
@RestController
public class ImAccessController {

    @Autowired
    private ImServiceImpl imService;

    @RequestMapping("push")
    public void push(String message, String uid) {
        SendChain chain = new SendChain();
        chain.addSend(new AppMsgSend());
        imService.setSendChain(chain);
        imService.send(message, uid);
    }

    @RequestMapping("pushByTag")
    public void pushByTag(String message, String tag) {
        SendChain chain = new SendChain();
        chain.addSend(new AppMsgSend());
        imService.setSendChain(chain);
        imService.sendGroupByTag(message, tag);
    }

    @RequestMapping("pushAll")
    public void pushAll(String message) {
        SendChain chain = new SendChain();
        chain.addSend(new AppMsgSend());
        imService.setSendChain(chain);
        imService.sendGroupAll(message);
    }

}
