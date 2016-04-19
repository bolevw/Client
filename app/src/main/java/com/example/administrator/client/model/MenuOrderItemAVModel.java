package com.example.administrator.client.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MenuOrderItemAVModel implements Serializable {


    private Integer num;
    private MenuModel model;

    public MenuOrderItemAVModel() {
    }

    public MenuOrderItemAVModel(Integer num, MenuModel model) {
        this.num = num;
        this.model = model;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
}
