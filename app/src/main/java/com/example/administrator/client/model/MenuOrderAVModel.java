package com.example.administrator.client.model;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MenuOrderAVModel extends AVObject {
    private String userId;

    private String tableNum;
    private List<MenuOrderItemAVModel> menuList;
    private String username;

    private Integer orderStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<MenuOrderItemAVModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuOrderItemAVModel> menuList) {
        this.menuList = menuList;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

}
