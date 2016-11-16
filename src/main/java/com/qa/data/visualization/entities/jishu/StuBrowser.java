package com.qa.data.visualization.entities.jishu;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUDENT_BROWER_LAST_TBL")
public class StuBrowser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String terminal;
    private String brower;
    private Integer cnt;

    public String getBrower() {
        return brower;
    }

    public void setBrower(String brower) {
        this.brower = brower;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
