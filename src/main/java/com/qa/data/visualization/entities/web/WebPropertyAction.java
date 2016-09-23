package com.qa.data.visualization.entities.web;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUD_PROPERTY_CHG_HIS_TBL")
public class WebPropertyAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String time;
    private String operatorid;
    private String operatorname;
    private String domain;
    private String notes;
    private String studentid;
    private String property;
    private String oldval;
    private String newval;
    private String recordid;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOldval() {
        return oldval;
    }

    public void setOldval(String oldval) {
        this.oldval = oldval;
    }

    public String getNewval() {
        return newval;
    }

    public void setNewval(String newval) {
        this.newval = newval;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

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


}
