package com.vcode.ticket.events;

import com.vcode.ticket.utils.ConfigUtils;
import com.vcode.ticket.utils.FileHelper;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: EventUtils
 * Author:   wangpeng.sy
 * Date:     2018/12/26 10:53
 * Description: ${DESCRIPTION}
 * History:
 */
public class EventUtils {

    private static EventManage eventManage=null;

    private EventUtils(){}

    public static synchronized EventManage newInstance(){
        if (eventManage == null){
            eventManage=new EventManageImpl();
            init();
        }
        return eventManage;
    }

    private static void init(){
        if (eventManage == null)
            return;

        //用搜索文件的方式去查找所有实现CustomEvent接口的类
        //当前事件较少目前暂不实现
        try {
            eventManage.register(new FileHelper());
            eventManage.register(ConfigUtils.getInstace());
        } catch (Exception e) {
            System.out.println("添加事件失败!"+e.getMessage());
        }
    }

}
