package com.qa.data.visualization.auth.entities;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role2 {
    private Long id;
    private String name;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}