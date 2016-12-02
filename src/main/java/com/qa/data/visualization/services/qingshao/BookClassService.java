package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.CostClass;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dykj on 2016/11/30.
 */
public interface BookClassService {
    LinkedHashMap<String, String> getBook(String data);

    ArrayList getBookMonthChoose(String data);

    LinkedHashMap<String, String> getBookChooseClassStock(String data);

    ArrayList getStudentLevels();

    ArrayList getBookRankChooseClass(String data);

    ArrayList getBookRankAge(String data);

    LinkedHashMap<String, String> getBookRankChooseClassCompare(String data);

    DataSet<CostClass> getBookChooseCostClass(String data, DatatablesCriterias criterias) throws ParseException;

    public Long getChooseBookCostClassCnt();

    public String getChooseBookWholeSql();
}
