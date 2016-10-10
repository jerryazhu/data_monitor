package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_DAILY_CLASS_NUM_TBL")
public class ClassModeDailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Integer getSkype() {
        return Skype;
    }

    public void setSkype(Integer skype) {
        Skype = skype;
    }

    public Integer getQQ() {
        return QQ;
    }

    public void setQQ(Integer QQ) {
        this.QQ = QQ;
    }

    public Integer getClassPlat() {
        return ClassPlat;
    }

    public void setClassPlat(Integer classPlat) {
        ClassPlat = classPlat;
    }

    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    private Integer Skype;
    private Integer QQ;
    private Integer ClassPlat;
    private Integer Total;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

}
