package com.vcode.ticket.utils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: LoggerUtils
 * Author:   wangpeng.sy
 * Date:     2018/12/26 17:13
 * Description: ${DESCRIPTION}
 * History:
 */
public class LoggerUtils {
    private static JTextArea textArea=initArea();

    private static SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");


    /**
     * 打印日志到界面 防止资源竞争造成死锁
     * @param message
     */
    public synchronized static void printLog(String message){
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    isNull();
                    System.out.println(dateFormat.format(new Date()) + "：" + message + "\r\n");
                    textArea.append(dateFormat.format(new Date()) + "：" + message + "\r\n");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化打印资源
     */
    private static JTextArea initArea(){
        JTextAreaExt textArea = new JTextAreaExt();
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3) {
                    textArea.setText("");
                    printLog("信息输出区清空完毕");
                }
            }
        });
        textArea.setEnabled(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBorder(BorderFactory.createTitledBorder("信息输出: "));
        return textArea;
    }

    private static void isNull(){
        if (textArea == null){
            throw new NullPointerException("打印资源未初始化");
        }
    }

    public static JTextArea getTextArea() {
        return textArea;
    }
}
