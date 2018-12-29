package com.vcode.ticket.model;

import org.json.JSONObject;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: TrainInfo
 * Author:   wangpeng.sy
 * Date:     2018/12/26 17:02
 * Description: ${DESCRIPTION}
 * History:
 */
public class TrainInfo {

    /**
     * 火车站 名称 和代码对应集合
     */
    public static JSONObject stationCode;
    /**
     * 查询出来的所有车次
     */
    public static List<String[]> trainInfoList=new LinkedList<>();

    /**
     * 是否刷票模式
     */
    public static boolean isBrushTicket=false;

    /**
     * 是否暂停刷票
     */
    public static boolean isPoolRun=false;

    /**
     * 选中座位名称列表( 一等座,二等座,硬座)
     */
    public static DefaultListModel<Object> seatLevel=new DefaultListModel<>();
}
