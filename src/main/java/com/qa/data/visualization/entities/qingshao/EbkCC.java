package com.qa.data.visualization.entities.qingshao;

import javax.persistence.*;

/**
 * Created by dykj on 2016/12/28.
 */
@Entity
@Table(name = "ebk_rbac_user")
public class EbkCC {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String department;
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
