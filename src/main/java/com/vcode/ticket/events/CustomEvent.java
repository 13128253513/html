package com.vcode.ticket.events;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: CustomEvent
 * Author:   wangpeng.sy
 * Date:     2018/12/26 10:10
 * Description: 所有实现该接口的类都会被通知
 * History:
 */
public interface CustomEvent {

    /**
     * 通知后执行动作
     * @param message
     */
    public void onChange(String message);
}
