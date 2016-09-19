package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUDENT_DAILY_ACTIVITY_TBL")
public class StuWebDailyActivity {
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
