package com.qa.data.visualization.entities.qingshao;

/**
 * Created by dykj on 2016/12/30.
 */
public class PayPercentConversion {
    private String ccid;
    private String ccname;
    private String ccgroup;
    private double allpercent;
    private double testpercent;
    private double personpercent;
    private double onepercent;
    private double buypercent;

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

    public double getAllpercent() {
        return allpercent;
    }

    public void setAllpercent(double allpercent) {
        this.allpercent = allpercent;
    }

    public double getTestpercent() {
        return testpercent;
    }

    public void setTestpercent(double testpercent) {
        this.testpercent = testpercent;
    }

    public double getPersonpercent() {
        return personpercent;
    }

    public void setPersonpercent(double personpercent) {
        this.personpercent = personpercent;
    }

    public double getOnepercent() {
        return onepercent;
    }

    public void setOnepercent(double onepercent) {
        this.onepercent = onepercent;
    }

    public double getBuypercent() {
        return buypercent;
    }

    public void setBuypercent(double buypercent) {
        this.buypercent = buypercent;
    }
}
