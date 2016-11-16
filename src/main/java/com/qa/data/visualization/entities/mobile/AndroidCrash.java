package com.qa.data.visualization.entities.mobile;

import javax.persistence.*;

/**
 * Created by dykj on 2016/11/15.
 */
@Entity
@Table(name="ABC360_ANDROID_APP_CRASH_TBL")
public class AndroidCrash {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String time;
    private String uid;
    private String mobile;
    private String unique_identifier;
    private String model;
    private String os_version;
    private String network_type;
    private String app_version;
    private String app_channel;
    private String md5;
    private String stack;
    private String logs;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUnique_identifier() {
        return unique_identifier;
    }

    public void setUnique_identifier(String unique_identifier) {
        this.unique_identifier = unique_identifier;
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

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_channel() {
        return app_channel;
    }

    public void setApp_channel(String app_channel) {
        this.app_channel = app_channel;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }




}
