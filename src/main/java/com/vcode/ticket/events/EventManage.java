package com.vcode.ticket.events;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: EventManage
 * Author:   wangpeng.sy
 * Date:     2018/12/26 10:13
 * Description: ${DESCRIPTION}
 * History:
 */
public interface EventManage {

    /**
     * 注册
     * @param customEvent
     */
    public void register(CustomEvent customEvent);

    /**
     * 注销
     * @param customEvent
     */
    public void unregister(CustomEvent customEvent);

    /**
     * 通知所有注册者
     * @param message
     */
    public void noticeAll(String message);
}
