package com.qa.data.visualization.entities.qingshao;

/**
 * Created by dykj on 2016/12/29.
 */
public class PayCCSaleMessage {
    private String sid;
    private String sname;
    private String ccid;
    private String ccname;
    private String ccgroup;
    private String money;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    public String getCcname() {
        return ccname;
    }

    public void setCcname(String ccname) {
        this.ccname = ccname;
    }

    public String getCcgroup() {
        return ccgroup;
    }

    public void setCcgroup(String ccgroup) {
        this.ccgroup = ccgroup;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
