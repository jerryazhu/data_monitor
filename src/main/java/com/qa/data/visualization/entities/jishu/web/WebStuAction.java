package com.qa.data.visualization.entities.jishu.web;

import com.web.spring.datatable.annotations.SqlIndex;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUDENT_ACTION_LAST_MONTH_WITH_TODAY_TBL")
public class WebStuAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SqlIndex
    private String time;
    @SqlIndex
    private String operatorid;
    private String operatorname;
    private String controller;
    @SqlIndex
    private String method;
    private String notes;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(String operatorid) {
        this.operatorid = operatorid;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
