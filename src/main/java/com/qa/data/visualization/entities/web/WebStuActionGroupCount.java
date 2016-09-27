package com.qa.data.visualization.entities.web;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUDENT_ACTION_COUNT_LAST_TBL")
public class WebStuActionGroupCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String controller;
    private String method;
    private String cnt;

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

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }


}
