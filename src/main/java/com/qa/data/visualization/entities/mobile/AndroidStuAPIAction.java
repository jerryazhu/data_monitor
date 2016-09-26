package com.qa.data.visualization.entities.mobile;

import com.qa.data.visualization.annotations.Index;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_APP_API_ANDROID_LAST_MONTH_WITH_TODAY_TBL")
public class AndroidStuAPIAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String time;
    @Index
    @Column(name = "uid")
    private String uid;
    private String client_ip;
    private String api_name;
    private String request_type;
    private String session_id;
    private String parameters;
    private String response_idx;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse_idx() {
        return response_idx;
    }

    public void setResponse_idx(String response_idx) {
        this.response_idx = response_idx;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
