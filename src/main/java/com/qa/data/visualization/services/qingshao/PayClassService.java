package com.qa.data.visualization.services.qingshao;

import com.mysql.fabric.xmlrpc.base.Data;
import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.entities.qingshao.PayCCMessage;
import com.qa.data.visualization.entities.qingshao.PayCCSaleMessage;
import com.qa.data.visualization.entities.qingshao.PayStudent;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dykj on 2016/12/26.
 */
public interface PayClassService {
    DataSet<PayStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<PayStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<PayCCMessage> getPayCCMessage(DatatablesCriterias criterias);

    DataSet<PayCCSaleMessage> getPayCcSaleMessage(String data,DatatablesCriterias criterias);

    ArrayList getCcGroup();

    List<AutoComplete> getCcAutoComplete(String query);

    Long getNewStudentCnt();

    Long getOldStudentCnt();

    String getNewStudentSql();

    String getOldStudentSql();

    String getNewStudentPayCnt();

    String getOldStudentPayCnt();

    String getCcSaleMessageSql();

    String getCcSaleMessageMoneyCnt();

    Long getCcSaleMessageCnt();
}
