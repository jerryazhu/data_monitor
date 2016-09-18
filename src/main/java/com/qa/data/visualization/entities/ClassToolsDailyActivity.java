package com.qa.data.visualization.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ABC360_CLASS_TOOLS_DAY_TBL")
public class ClassToolsDailyActivity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String time;
    private String type;
    private Integer count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
