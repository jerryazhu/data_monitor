package com.qa.data.visualization.entities.qingshao;

import javax.persistence.*;

/**
 * Created by dykj on 2016/11/21.
 */
@Entity
@Table(name = "ebk_teachers")
public class EbkTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
