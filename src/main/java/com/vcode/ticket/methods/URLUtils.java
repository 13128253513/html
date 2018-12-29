package com.vcode.ticket.methods;


import com.alibaba.fastjson.TypeReference;
import com.vcode.ticket.utils.FileHelper;
import com.vcode.ticket.utils.URLIndex;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: URLUtils
 * Author:   wangpeng.sy
 * Date:     2018/12/25 10:45
 * Description: ${DESCRIPTION}
 * History:
 */
public class URLUtils {

    private static Map<String,URLIndex> urlLocal=FileHelper.readJsonData(URLUtils.class.getResource("/urlConfig.txt").getFile(),new TypeReference<HashMap<String,URLIndex>>(){});

    /**
     * 获取验证码验证码
     * @return
     */
    public static URLIndex getVerificationCode(){
        return get("verificationCode",URLManageEnum.verification_code);
    }

    /**
     * 验证 验证码是否正确
     * @return
     */
    public static URLIndex getCheckCode(){
        return get("checkCode",URLManageEnum.check_code);
    }

    /**
     * 获取登录url
     * @return
     */
    public static URLIndex getLogin(){
        return get("login",URLManageEnum.login);
    }

    /**
     * 验证登录是否成功
     * @return
     */
    public static URLIndex getCheckLogin(){
        return get("checkLogin",URLManageEnum.check_login);
    }

    /**
     * 初始化界面,获取session
     * @return
     */
    public static URLIndex getInitHtml(){
        return get("initHtml",URLManageEnum.init_html);
    }

    /**
     * 初始化乘车人信息
     * @return
     */
    public static URLIndex getInitBusPeople(){
        return get("initBusPeople",URLManageEnum.init_bus_people);
    }

    /**
     * 初始化火车票信息
     * @return
     */
    public static URLIndex getInitTrainTicketInfo(){
        return get("initTrainTicketInfo",URLManageEnum.init_train_ticket_info);
    }

    /**
     * 预订按钮 初始化订单界面
     * @return
     */
    public static URLIndex getInitSubmitOrder(){
        return get("initSubmitOrder",URLManageEnum.inint_submit_order);
    }

    /**
     * 验证订单信息
     * @return
     */
    public static URLIndex getCheckOrderInfo(){
        return get("checkOrderInfo",URLManageEnum.check_order_info);
    }

    /**
     * 验证订单信息
     * @return
     */
    public static URLIndex getCheckQueueCount(){
        return get("checkQueueCount",URLManageEnum.check_queue_count);
    }

    /**
     * 提交订单
     * @return
     */
    public static URLIndex getSubmitOrder(){
        return get("submitOrder",URLManageEnum.submit_order);
    }

    /**
     * 查询订单信息
     * @return
     */
    public static URLIndex getQueryOrder(){
        return get("queryOrder",URLManageEnum.query_order);
    }

    /**
     * 查询预订订单信息及等待时间
     * @return
     */
    public static URLIndex getQueryOrderWaitTime(){
        return get("queryOrderWaitTime",URLManageEnum.query_order_wait_time);
    }

    private static URLIndex get(String key,URLManageEnum defaultURL){
        URLIndex vlaue=urlLocal.get(key);
        if (vlaue == null){
            vlaue=new URLIndex(defaultURL);
            urlLocal.put(key,vlaue);
        }
        return vlaue;
    }


    public static Map<String, URLIndex> getURLCache() {
        return urlLocal;
    }
}
