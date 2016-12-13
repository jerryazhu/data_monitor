package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.services.qingshao.BookClassService;
import com.qa.data.visualization.services.qingshao.ClassService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by dykj on 2016/11/17.
 */
@Controller
@RequestMapping(value = "/class")
public class CostClassController {
    @Autowired
    private ClassService classService;
    @Autowired
    private BookClassService bookClassService;

    @RequestMapping(value = "/student_auto_complete")
    @ResponseBody
    public List<AutoComplete> autocompleteStudent(@RequestParam(value = "query", required = true) String query, HttpServletRequest request) {
        return classService.getStudentAutoComplete(query);
    }

    @RequestMapping(value = "/teacher_auto_complete")
    @ResponseBody
    public List<AutoComplete> autocompleteTeacher(@RequestParam(value = "query", required = true) String query, HttpServletRequest request) {
        return classService.getTeacherAutoComplete(query);
    }

    @RequestMapping("/get_teacher_group")
    @ResponseBody
    public ArrayList getTeacherGroup() {
        return classService.getTeacherGroup();
    }

    @RequestMapping("/get_sa_teacher_group")
    @ResponseBody
    public ArrayList getSaTeacherGroup() {
        return classService.getSaTeacherGroup();
    }

    @RequestMapping("/get_cost_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostClass> getCostClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostClass> dataSet = classService.getCostClass(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_cost_sa_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostSaClass> getCostSaClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostSaClass> dataSet = classService.getCostSaClass(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_new_student/{data}")
    @ResponseBody
    public DatatablesResponse<payStudent> getNewStudent(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<payStudent> dataSet = classService.getNewStudent(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_old_student/{data}")
    @ResponseBody
    public DatatablesResponse<payStudent> getOldStudent(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<payStudent> dataSet = classService.getOldStudent(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_book_choose_cost_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostClass> getBookChooseCostClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostClass> dataSet = bookClassService.getBookChooseCostClass(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_cost_class_cnt")
    @ResponseBody
    public Long getCostClassCnt() {
        return classService.getCostClassCnt();
    }

    @RequestMapping("/get_cost_sa_class_cnt")
    @ResponseBody
    public Long getCostSaClassCnt() {
        return classService.getCostSaClassCnt();
    }

    @RequestMapping("/get_new_student_cnt")
    @ResponseBody
    public Long getNewStudentCnt() {
        return classService.getNewStudentCnt();
    }

    @RequestMapping("/get_old_student_cnt")
    @ResponseBody
    public Long getOldStudentCnt() {
        return classService.getOldStudentCnt();
    }

    @RequestMapping("/get_book_choose_cost_class_cnt")
    @ResponseBody
    public Long getChooseBookCostClassCnt() {
        return bookClassService.getChooseBookCostClassCnt();
    }

    @RequestMapping("/get_whole_sql")
    @ResponseBody
    public String getWholeSql() {
        return classService.getWholeSql();
    }

    @RequestMapping("/get_whole_sa_sql")
    @ResponseBody
    public String getWholeSaSql() {
        return classService.getWholeSaSql();
    }

    @RequestMapping("/get_new_student_sql")
    @ResponseBody
    public String getNewStudentSql() {
        return classService.getNewStudentSql();
    }

    @RequestMapping("/get_old_student_sql")
    @ResponseBody
    public String getOldStudentSql() {
        return classService.getOldStudentSql();
    }

    @RequestMapping("/get_new_student_pay_cnt")
    @ResponseBody
    public String getNewStudentPayCnt() {
        return classService.getNewStudentPayCnt();
    }

    @RequestMapping("/get_old_student_pay_cnt")
    @ResponseBody
    public String getOldStudentPayCnt() {
        return classService.getOldStudentPayCnt();
    }

    @RequestMapping("/get_book_choose_cost_class_sql")
    @ResponseBody
    public String getChooseBookWholeSql() {
        return bookClassService.getChooseBookWholeSql();
    }

    @RequestMapping("/get_book/{data}")
    @ResponseBody
    public ArrayList getBook(@PathVariable String data) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> book = bookClassService.getBook(data);
        for (Map.Entry<String, String> entry : book.entrySet()) {
            Object[] array = new Object[2];
            array[0] = entry.getKey();
            array[1] = Integer.parseInt(entry.getValue());
            list.add(array);
        }
        return list;
    }

    @RequestMapping("/get_book_month_choose/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public HashMap getBookMonthChoose(@PathVariable String data) throws Exception {
        ArrayList message = bookClassService.getBookMonthChoose(data);
        HashMap bookMessage = new HashMap();
        bookMessage.put("xName", message.get(0));
        bookMessage.put("type0", message.get(1));
        bookMessage.put("type1", message.get(2));
        return bookMessage;
    }

    @RequestMapping("/get_book_month_choose_table/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getBookMonthChooseTable(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<BookMonthChooseTable> actions = bookClassService.getBookMonthChooseTable(criterias, data);
        return DatatablesResponse.build(actions, criterias);
    }
    @RequestMapping(value = "/get_book_choose_class_stock/{data}")
    @ResponseBody
    public ArrayList getBookChooseClassStock(@PathVariable String data) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        String now = String.valueOf(System.currentTimeMillis());
        LinkedHashMap<String, String> payRegionStock = bookClassService.getBookChooseClassStock(data);
        for (Map.Entry<String, String> entry : payRegionStock.entrySet()) {
            Object[] array = new Object[2];
            String time = entry.getKey();
            String count = entry.getValue();
            array[0] = Long.parseLong(time);
            array[1] = Integer.parseInt(count);
            list.add(array);
        }
        return list;
    }

    @RequestMapping(value = "/get_book_choose_class_stock_sql/{data}")
    @ResponseBody
    public DatatablesResponse getBookChooseClassStockSql(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<BookChooseClassStock> actions = bookClassService.getBookChooseClassStockSql(criterias, data);
        String[] cutData = data.split("---");
        if (cutData[2].equals("sql")) {
            return null;
        } else {
            return DatatablesResponse.build(actions, criterias);
        }
    }
    @RequestMapping(value = "/get_student_levels")
    @ResponseBody
    public ArrayList getStudentLevels() throws Exception {
        return bookClassService.getStudentLevels();
    }

    @RequestMapping(value = "/get_book_rank_choose_class/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public HashMap getBookRankChooseClass(@PathVariable String data) throws Exception {
        ArrayList message = bookClassService.getBookRankChooseClass(data);
        HashMap bookClassMessage = new HashMap();
        bookClassMessage.put("xName", message.get(0));
        bookClassMessage.put("type0", message.get(1));
        bookClassMessage.put("type1", message.get(2));
        return bookClassMessage;
    }

    @RequestMapping(value = "/get_book_rank_choose_class_table/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getBookRankChooseClassTable(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<BookRankChooseClassTable> actions = bookClassService.getBookRankChooseClassTable(criterias, data);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_book_rank_age_table/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getBookRankAgeTable(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<BookRankAgeTable> actions = bookClassService.getBookRankAgeTable(criterias, data);
        return DatatablesResponse.build(actions, criterias);
    }
    @RequestMapping(value = "/get_book_rank_age/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public HashMap getBookRankAge(@PathVariable String data) throws Exception {
        ArrayList message = bookClassService.getBookRankAge(data);
        HashMap bookClassMessage = new HashMap();
        bookClassMessage.put("xName", message.get(0));
        for (int i = 0; i < 10; i++) {
            bookClassMessage.put("type" + i, message.get(i + 1));
        }
        return bookClassMessage;
    }

    @RequestMapping(value = "/get_book_rank_choose_class_compare/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ArrayList getBookRankChooseClassCompare(@PathVariable String data) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> bookRankChooseClassCompare = bookClassService.getBookRankChooseClassCompare(data);
        for (Map.Entry<String, String> entry : bookRankChooseClassCompare.entrySet()) {
            Object[] array = new Object[2];
            String time = entry.getKey();
            String count = entry.getValue();
            array[0] = Long.parseLong(time);
            array[1] = Integer.parseInt(count);
            list.add(array);
        }
        return list;
    }

    @RequestMapping(value = "/get_book_rank_choose_class_compare_sql/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getBookRankChooseCompareSql(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<BookChooseClassStock> actions = bookClassService.getBookRankChooseCompareSql(criterias, data);
        String[] cutData = data.split("---");
        if (cutData[cutData.length - 1].equals("sql")) {
            return null;
        } else {
            return DatatablesResponse.build(actions, criterias);
        }
    }

}