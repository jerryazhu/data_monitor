package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_APP_API_CLASSPLAT_STUDENT_ACTION_COUNT_YESTEADAY_TBL")
public class PCAPIStuActionGroupCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
