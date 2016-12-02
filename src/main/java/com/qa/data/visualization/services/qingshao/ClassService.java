package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.entities.qingshao.CostClass;
import com.qa.data.visualization.entities.qingshao.CostSaClass;
import com.qa.data.visualization.entities.qingshao.payStudent;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
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

    Long getCostClassCnt();

    Long getCostSaClassCnt();

    Long getNewStudentCnt();

    Long getOldStudentCnt();

    String getWholeSql();

    String getWholeSaSql();

    String getNewStudentSql();

    String getOldStudentSql();

    String getNewStudentPayCnt();

    String getOldStudentPayCnt();
}
