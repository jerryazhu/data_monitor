package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.CostClass;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/11/30.
 */
public interface BookClassService {
    LinkedHashMap<String, String> getBook(String data) throws ParseException;

    ArrayList getBookMonthChoose(String data) throws ParseException;

    LinkedHashMap<String, String> getBookChooseClassStock(String data) throws ParseException;

    List<Object[]> getBookChooseClassStockSql(String data) throws ParseException;

    ArrayList getStudentLevels();

    ArrayList getBookRankChooseClass(String data) throws ParseException;

    ArrayList getBookRankAge(String data) throws ParseException;

    LinkedHashMap<String, String> getBookRankChooseClassCompare(String data) throws ParseException;

    List<Object[]> getBookRankChooseCompareSql(String data) throws ParseException;

    DataSet<CostClass> getBookChooseCostClass(String data, DatatablesCriterias criterias) throws ParseException;

    public Long getChooseBookCostClassCnt();

    public String getChooseBookWholeSql();
}
