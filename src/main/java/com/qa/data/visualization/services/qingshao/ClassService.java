package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/11/17.
 */
public interface ClassService {
    List<AutoComplete> getStudentAutoComplete(String query);

    List<AutoComplete> getTeacherAutoComplete(String query);

    ArrayList getTeacherGroup();

    ArrayList getSaTeacherGroup();

    DataSet<CostClass> getCostClass(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<CostSaClass> getCostSaClass(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<payStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<payStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<WorkStudentMessage> getWorkStudentMessage(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<WorkStudentRecommend> getWorkStudentRecommend(String data, DatatablesCriterias criterias);

    DataSet<WorkClassMessage> getWorkClassMessage(String data,DatatablesCriterias criterias);

    DataSet<WorkLoseStudentClass> getWorkLoseStudentClass(String data,DatatablesCriterias criterias);

    DataSet<WorkLoseStudentAcoin> getWorkLoseStudentAcoin(String data,DatatablesCriterias criterias);

    DataSet<WorkLoseStudentAcoin> getWorkLoseStudentAcoinCnt(String data,DatatablesCriterias criterias);

    DataSet getWorkRenew(String data,DatatablesCriterias criterias);

    DataSet <WorkRefunds> getWorkRefunds(String data,DatatablesCriterias criterias);

    Long getCostClassCnt();

    Long getCostSaClassCnt();

    Long getNewStudentCnt();

    Long getOldStudentCnt();

    Long getWorkStudentMessageCnt();

    Long getWorkClassMessageCnt();

    ArrayList getWorkRefundsCnt();

    String getWholeSql();

    String getWholeSaSql();

    String getNewStudentSql();

    String getOldStudentSql();

    String getNewStudentPayCnt();

    String getOldStudentPayCnt();

    String getWorkStudentMessageSql();

    String getWorkClassMessageSql();

    String getWorkRenewSql();

    String getWorkRefundsSql();

    String getWorkLoseClassSql();

    Long getWorkLoseClassCnt();

    ArrayList getWorkRenewCnt();

    ArrayList getStudentLoseDay(String data);

    LinkedHashMap<String ,String> getDayStudentActivityChart(String data);
}
