package com.qa.data.visualization.entities.jishu.mobile;

import javax.persistence.*;

public class AndroidAPIStuActionGroupCount {
    private String api_name;
    private Integer cnt;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }


}
