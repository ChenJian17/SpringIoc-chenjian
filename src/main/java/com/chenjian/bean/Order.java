package com.chenjian.bean;

/**
 * Created by chenjian on 2021/6/11 21:09
 */

public class Order {

    private int oid;
    private String title;
    private String createtime;
    private double money;

    public Order() {
    }

    public Order(int oid, String title, String createtime, double money) {
        this.oid = oid;
        this.title = title;
        this.createtime = createtime;
        this.money = money;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
