package com.example.administrator.client.model;

/**
 * Created by Administrator on 2016/4/21.
 */
public class OrderHistoryModel {
    private String id;
    private String money;
    private String date;
    private String tableNum;


    public OrderHistoryModel() {
    }

    public OrderHistoryModel(String id, String money, String date, String tableNum) {
        this.id = id;
        this.money = money;
        this.date = date;
        this.tableNum = tableNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }
}
