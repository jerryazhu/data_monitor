package com.qa.data.visualization.entities.callcenter;

import javax.persistence.*;

@Entity
@Table(name = "agentcdr")
public class CallPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String calldata;
    private String duration;
    private String billsec;
    private String callagent;

    public String getCalldata() {
        return calldata;
    }

    public void setCalldata(String calldata) {
        this.calldata = calldata;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBillsec() {
        return billsec;
    }

    public void setBillsec(String billsec) {
        this.billsec = billsec;
    }

    public String getCallagent() {
        return callagent;
    }

    public void setCallagent(String callagent) {
        this.callagent = callagent;
    }
}
