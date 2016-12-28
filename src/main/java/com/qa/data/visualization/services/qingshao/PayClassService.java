package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.PayCCMessage;
import com.qa.data.visualization.entities.qingshao.PayStudent;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;

/**
 * Created by dykj on 2016/12/26.
 */
public interface PayClassService {
    DataSet<PayStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<PayStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<PayCCMessage> getPayCCMessage(DatatablesCriterias criterias);

    Long getNewStudentCnt();

    Long getOldStudentCnt();

    String getNewStudentSql();

    String getOldStudentSql();

    String getNewStudentPayCnt();

    String getOldStudentPayCnt();
}
