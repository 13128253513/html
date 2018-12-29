package com.vcode.ticket.events;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: EventManageImpl
 * Author:   wangpeng.sy
 * Date:     2018/12/26 10:18
 * Description: ${DESCRIPTION}
 * History:
 */

public class EventManageImpl implements EventManage{

    protected EventManageImpl(){

    }

    /**
     * 存放被通知对象
     */
    private ArrayList<WeakReference> obj = new ArrayList<WeakReference>();

    @Override
    public void register(CustomEvent customEvent) {
        obj.add(new WeakReference(customEvent));
    }

    @Override
    public void unregister(CustomEvent customEvent) {
        obj.remove(new WeakReference(customEvent));
    }

    @Override
    public void noticeAll(String message) {
        for (WeakReference event : obj){
            CustomEvent customEvent= (CustomEvent) event.get();
            if (customEvent != null){
                customEvent.onChange(message);
            }else {
                obj.remove(event);
            }
        }
    }
}
