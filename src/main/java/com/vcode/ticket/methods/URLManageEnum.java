package com.vcode.ticket.methods;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: URLManage
 * Author:   wangpeng.sy
 * Date:     2018/12/25 10:34
 * Description: ${DESCRIPTION}
 * History:
 */
public enum  URLManageEnum {

    /**
     * 登录界面 获取验证码
     */
    verification_code(new String[]{"https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&"}),

    /**
     * 验证 验证码
     */
    check_code(new String[]{"https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn"}),

    /**
     * 登录
     */
    login(new String[]{"https://kyfw.12306.cn/otn/login/loginAysnSuggest"}),

    /**
     * 验证登录是否成功,是否到首页
     */
    check_login(new String[]{"https://kyfw.12306.cn/otn/login/userLogin"}),


    /**
     * 初始化乘客
     */
    init_bus_people(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs"}),
    /**
     * 查询车次按钮
     */
    init_train_ticket_info(new String[]{"https://kyfw.12306.cn/otn/leftTicket/queryA?","https://kyfw.12306.cn/otn/leftTicket/queryZ?","https://kyfw.12306.cn/otn/leftTicket/queryX?","https://kyfw.12306.cn/otn/leftTicket/query?"}),

    /**
     * 预订按钮
     */
    inint_submit_order(new String[]{"https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest"}),

    /**
     * 初始化界面,用于获取session
     */
    init_html(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/initDc"}),

    /**
     * 验证火车订单信息
     */
    check_order_info(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo"}),

    /**
     * 验证是否有余票
     */
    check_queue_count(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount"}),

    /**
     * 提交订单
     */
    submit_order(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue"}),

    /**
     * 查询预订车票信息及等待时间
     */
    query_order_wait_time(new String[]{"https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime?"}),

    /**
     * 查询订单
     */
    query_order(new String[]{"https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete"});



    private String[] url;

    URLManageEnum(String[] url){
        this.url=url;
    }

    public java.lang.String[] getUrl() {
        return url;
    }

    public void setUrl(java.lang.String[] url) {
        this.url = url;
    }
}
