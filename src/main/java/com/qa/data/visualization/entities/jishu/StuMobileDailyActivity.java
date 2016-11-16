package com.qa.data.visualization.entities.jishu;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_APP_API_Daily_ACTIVITY_TBL")
public class StuMobileDailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer type;
    private Long day;
    private Integer count;

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
