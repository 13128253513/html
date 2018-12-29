package com.vcode.ticket.utils;

import com.alibaba.fastjson.annotation.JSONField;
import com.vcode.ticket.methods.URLManageEnum;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: URLIndex
 * Author:   wangpeng.sy
 * Date:     2018/12/25 11:05
 * Description: ${DESCRIPTION}
 * History:
 */
public class URLIndex implements Serializable{
    private static final long serialVersionUID = 1311701041370094156L;

    private URLManageEnum key;

    private transient String url;

    private AtomicInteger index=new AtomicInteger(0);

    public URLIndex(){
    }

    public URLIndex(URLManageEnum url){
        this.key=url;
    }

    public URLIndex OK(){
        this.url= currentURL();
        return this;
    }

    public synchronized URLIndex isFail(){
        if (key.getUrl().length-1 > index.get()){
            index.incrementAndGet();
        }else {
            index.set(0);//初始化重新遍历;
        }

        return this;
    }

    private static boolean isNull(String value){
        return value == null || value.trim().length()==0;
    }

    public synchronized String currentURL() {
        if (isNull(this.url)){
            return this.key.getUrl()[this.index.get()];
        }else {
            return this.url;
        }
    }


    public URLManageEnum getKey() {
        return key;
    }

    public void setKey(URLManageEnum key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AtomicInteger getIndex() {
        return index;
    }

    public void setIndex(AtomicInteger index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "URLIndex{" +
                "key=" + key +
                ", url='" + url + '\'' +
                ", index=" + index +
                '}';
    }
}
