package com.example.administrator.client.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 */
public class SearchResultModel implements Serializable {


    private String id;
    private String imageSrc;
    private String name;
    private String detail; //材料
    private String function; //功能
    private String money;
    private String type; //菜系
    private String taste; //口味
    private String method; // 做法
    private boolean has;
    private int menuStatus = 1;

    public SearchResultModel() {
    }

    public SearchResultModel(String id, String imageSrc, String name, String detail, String function, String money, String type, String taste, String method, boolean has, int menuStatus) {
        this.id = id;
        this.imageSrc = imageSrc;
        this.name = name;
        this.detail = detail;
        this.function = function;
        this.money = money;
        this.type = type;
        this.taste = taste;
        this.method = method;
        this.has = has;
        this.menuStatus = menuStatus;
    }

    public int getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(int menuStatus) {
        this.menuStatus = menuStatus;
    }

    public boolean isHas() {
        return has;
    }

    public void setHas(boolean has) {
        this.has = has;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "MenuModel{" +
                "id='" + id + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", function='" + function + '\'' +
                '}';
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
