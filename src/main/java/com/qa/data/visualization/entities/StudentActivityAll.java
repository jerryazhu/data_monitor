package com.qa.data.visualization.entities;

import javax.persistence.*;

/**
 * Created by dykj on 2016/11/4.
 */
@Entity
@Table(name="ABC360_STUDENT_DAILY_ACTIVITY_TBL")
public class StudentActivityAll {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String day;
    private Integer count;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

