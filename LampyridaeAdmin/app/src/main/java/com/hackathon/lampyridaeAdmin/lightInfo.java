package com.hackathon.lampyridaeAdmin;

import java.io.Serializable;

public class lightInfo implements Serializable {
    String lightnum;
    String address;
    String manage;
    String manageNumber;
    int count;

    public lightInfo(){
    }

    public lightInfo(String lightnum, String address, String manage, String manageNumber) {
        this.lightnum = lightnum;
        this.address = address;
        this.manage = manage;
        this.manageNumber = manageNumber;
    }

    public lightInfo(String lightnum, String address, String manage, String manageNumber, int count) {
        this.lightnum = lightnum;
        this.address = address;
        this.manage = manage;
        this.manageNumber = manageNumber;
        this.count = count;
    }

    public String getLightnum() {
        return lightnum;
    }

    public void setLightnum(String lightnum) {
        this.lightnum = lightnum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public String getManageNumber() {
        return manageNumber;
    }

    public void setManageNumber(String manageNumber) {
        this.manageNumber = manageNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return lightnum + "," + address +","+manage+","+manageNumber+","+count;
    }
}