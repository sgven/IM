package demo.im.system.controller;

import demo.im.system.service.ImService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 消息服务Controller
 */
@Slf4j
@Scope("prototype")
@Controller
public class ImController {

    @Autowired
    private ImService imService;

    /**
     * 根据用户，获取消息列表
     *
     * 支持参数说明：
     * @Param   isRead  是否已读，0未读，1已读，未传值则查全部
     * @Param   searchText  搜索关键词
     */
    public void getMsgList() {

    }

    /**
     * 标记为已读
     */
    public void markRead() {

    }

    /**
     * 标记为未读
     */
    public void markNoRead() {

    }

    /**
     * 一键已读
     */
    public void markReadAll() {

    }

    /**
     * 一键清除
     */
    public void clearMsgAll() {

    }
}
