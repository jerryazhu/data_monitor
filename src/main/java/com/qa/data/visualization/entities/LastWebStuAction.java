package com.qa.data.visualization.entities;

import javax.persistence.*;

@Entity
@Table(name = "ABC360_WEB_ERROR_LAST_TBL")
public class LastWebStuAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
