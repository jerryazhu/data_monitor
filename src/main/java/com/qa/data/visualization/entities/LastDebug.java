package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_DEBUG_LAST_TBL")
public class LastDebug {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
