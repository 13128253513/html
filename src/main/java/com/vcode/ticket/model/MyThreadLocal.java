package com.vcode.ticket.model;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: MyThreadLocal
 * Author:   wangpeng.sy
 * Date:     2018/12/26 16:22
 * Description: ${DESCRIPTION}
 * History:
 */
public class MyThreadLocal {

    private static ThreadLocal<UserInfo> local=new InheritableThreadLocal<>();

    public static UserInfo get(){
        return local.get();
    }

    public static void set(UserInfo info){
        local.set(info);
    }

    public static void reset(){
        local.remove();
    }
}
