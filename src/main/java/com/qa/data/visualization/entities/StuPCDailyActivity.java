package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ebk_pc_stu_client")
public class StuPCDailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer sid;
    private String os;
    private Integer ip;
    private String city;
    private String version;
    private Integer login_time;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Integer getIp() {
        return ip;
    }

    public void setIp(Integer ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Integer login_time) {
        this.login_time = login_time;
    }
}
