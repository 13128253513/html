package com.vcode.ticket.model;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: UserInfo
 * Author:   wangpeng.sy
 * Date:     2018/12/26 16:18
 * Description: ${DESCRIPTION}
 * History:
 */
public class UserInfo {

    /**
     * 始发站
     */
    private String fromStation;
    /**
     * 到达站
     */
    private String toStation;

    /**
     * 验证码
     */
    private String validCode;

    /**
     * 当前选中抢票乘客
     */
    private List<String> searchPassengerName;
    /**
     * 当前选中抢票座位转义代码
     */
    private List<Integer> searchSeat;

    /**
     * 当前选中抢票车次
     */
    private List<String> searchTrains;
    /**
     * 当前线程查询的车次
     */
    private String currentThreadTrain;

    /**
     * 当前用户拥有乘客数据集合
     */
    private Map<String,JSONObject> passenger;

    /**
     * 火车票开始时间
     */
    private String trainDate;

    /**
     * 火车票结束时间
     */
    private String backTrainDate;

    private String keyCheckisChange;

    /**
     * 秘钥 相当于session
     */
    private String token;


    /**
     * 生成查询火车票信息请求入参
     * @return
     */
    public String createQueryRequestParam(){
        StringBuffer sb = new StringBuffer();
        sb.append("leftTicketDTO.train_date=");
        sb.append(this.trainDate).append("&");
        sb.append("leftTicketDTO.from_station=");
        sb.append(this.fromStation).append("&");
        sb.append("leftTicketDTO.to_station=");
        sb.append(this.toStation).append("&");
        sb.append("purpose_codes=ADULT");
        return sb.toString();
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }


    public Map<String, JSONObject> getPassenger() {
        return passenger;
    }

    public void setPassenger(Map<String, JSONObject> passenger) {
        this.passenger = passenger;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getBackTrainDate() {
        return backTrainDate;
    }

    public void setBackTrainDate(String backTrainDate) {
        this.backTrainDate = backTrainDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKeyCheckisChange() {
        return keyCheckisChange;
    }

    public void setKeyCheckisChange(String keyCheckisChange) {
        this.keyCheckisChange = keyCheckisChange;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public List<String> getSearchPassengerName() {
        return searchPassengerName;
    }

    public void setSearchPassengerName(List<String> searchPassengerName) {
        this.searchPassengerName = searchPassengerName;
    }

    public List<Integer> getSearchSeat() {
        return searchSeat;
    }

    public void setSearchSeat(List<Integer> searchSeat) {
        this.searchSeat = searchSeat;
    }

    public List<String> getSearchTrains() {
        return searchTrains;
    }

    public void setSearchTrains(List<String> searchTrains) {
        this.searchTrains = searchTrains;
    }

    public String getCurrentThreadTrain() {
        return currentThreadTrain;
    }

    public void setCurrentThreadTrain(String currentThreadTrain) {
        this.currentThreadTrain = currentThreadTrain;
    }
}
