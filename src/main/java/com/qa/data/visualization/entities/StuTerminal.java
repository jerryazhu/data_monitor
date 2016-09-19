package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_STUDENT_TERMINAL_LAST_TBL")
public class StuTerminal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String terminal;

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

    private Integer cnt;
}
