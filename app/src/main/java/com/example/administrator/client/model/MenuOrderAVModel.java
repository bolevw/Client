package com.example.administrator.client.model;

import com.avos.avoscloud.AVObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MenuOrderAVModel extends AVObject implements Serializable {
    private String userId;

    private String tableNum;

    public List<MenuOrderItemModel> getItemModel() {
        return itemModel;
    }

    public void setItemModel(List<MenuOrderItemModel> itemModel) {
        this.itemModel = itemModel;
    }

    private List<MenuOrderItemModel> itemModel;

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
