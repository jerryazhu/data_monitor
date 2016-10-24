package com.qa.data.visualization.entities.mobile;

import com.web.spring.datatable.annotations.SqlCondition;
import com.web.spring.datatable.annotations.SqlIndex;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_ANDROID_APP_DEVICE_TBL")
public class AndroidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @SqlIndex
    @SqlCondition("LENGTH(time)=13 and time<UNIX_TIMESTAMP(now())*1000 and time> (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000)")
    private String time;
    @SqlIndex
    private String uid;
    private String model;
    private String os_version;
    private String app_version;
    private String nickName;

    public String getTime() {
        return time;
    }

    public void setTime(String api_name) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}