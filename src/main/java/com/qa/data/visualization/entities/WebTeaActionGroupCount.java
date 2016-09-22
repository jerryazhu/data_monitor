package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_TEACHER_ACTION_COUNT_LAST_TBL")
public class WebTeaActionGroupCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String controller;
    private String method;
    private Integer cnt;

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }


}
