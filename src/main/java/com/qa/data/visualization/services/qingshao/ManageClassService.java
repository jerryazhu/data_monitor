package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dykj on 2016/11/17.
 */
public interface ManageClassService {
    List<AutoComplete> getStudentAutoComplete(String query);

    List<AutoComplete> getTeacherAutoComplete(String query);

    ArrayList getTeacherGroup();

    ArrayList getSaTeacherGroup();

    DataSet<ManagerCostClass> getCostClass(String data, DatatablesCriterias criterias) throws ParseException;

    DataSet<ManagerCostSaClass> getCostSaClass(String data, DatatablesCriterias criterias) throws ParseException;

    Long getCostClassCnt();

    Long getCostSaClassCnt();

    String getWholeSql();

    String getWholeSaSql();

}
