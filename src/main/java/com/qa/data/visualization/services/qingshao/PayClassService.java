package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.payStudent;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;

/**
 * Created by dykj on 2016/12/26.
 */
public interface PayClassService {
    DataSet<payStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<payStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException;

    Long getNewStudentCnt();

    Long getOldStudentCnt();

    String getNewStudentSql();

    String getOldStudentSql();

    String getNewStudentPayCnt();

    String getOldStudentPayCnt();
}
