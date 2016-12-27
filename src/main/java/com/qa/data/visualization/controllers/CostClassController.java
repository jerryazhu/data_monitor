package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.services.qingshao.BookClassService;
import com.qa.data.visualization.services.qingshao.ManageClassService;
import com.qa.data.visualization.services.qingshao.PayClassService;
import com.qa.data.visualization.services.qingshao.WorkClassService;
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
    private ManageClassService manageClassService;
    @Autowired
    private BookClassService bookClassService;
    @Autowired
    private PayClassService payClassService;
    @Autowired
    private WorkClassService workClassService;

    @RequestMapping(value = "/student_auto_complete")
    @ResponseBody
    public List<AutoComplete> autocompleteStudent(@RequestParam(value = "query", required = true) String query, HttpServletRequest request) {
        return manageClassService.getStudentAutoComplete(query);
    }

    @RequestMapping(value = "/teacher_auto_complete")
    @ResponseBody
    public List<AutoComplete> autocompleteTeacher(@RequestParam(value = "query", required = true) String query, HttpServletRequest request) {
        return manageClassService.getTeacherAutoComplete(query);
    }

    @RequestMapping("/get_teacher_group")
    @ResponseBody
    public ArrayList getTeacherGroup() {
        return manageClassService.getTeacherGroup();
    }

    @RequestMapping("/get_sa_teacher_group")
    @ResponseBody
    public ArrayList getSaTeacherGroup() {
        return manageClassService.getSaTeacherGroup();
    }

    @RequestMapping("/get_cost_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostClass> getCostClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostClass> dataSet = manageClassService.getCostClass(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_cost_sa_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostSaClass> getCostSaClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostSaClass> dataSet = manageClassService.getCostSaClass(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_new_student/{data}")
    @ResponseBody
    public DatatablesResponse<payStudent> getNewStudent(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<payStudent> dataSet = payClassService.getNewStudent(data, criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping("/get_old_student/{data}")
    @ResponseBody
    public DatatablesResponse<payStudent> getOldStudent(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<payStudent> dataSet = payClassService.getOldStudent(data, criterias);
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
        return manageClassService.getCostClassCnt();
    }

    @RequestMapping("/get_cost_sa_class_cnt")
    @ResponseBody
    public Long getCostSaClassCnt() {
        return manageClassService.getCostSaClassCnt();
    }

    @RequestMapping("/get_new_student_cnt")
    @ResponseBody
    public Long getNewStudentCnt() {
        return payClassService.getNewStudentCnt();
    }

    @RequestMapping("/get_old_student_cnt")
    @ResponseBody
    public Long getOldStudentCnt() {
        return payClassService.getOldStudentCnt();
    }

    @RequestMapping("/get_book_choose_cost_class_cnt")
    @ResponseBody
    public Long getChooseBookCostClassCnt() {
        return bookClassService.getChooseBookCostClassCnt();
    }

    @RequestMapping("/get_work_student_message_cnt")
    @ResponseBody
    public Long getWorkStudentMessageCnt() {
        return workClassService.getWorkStudentMessageCnt();
    }

    @RequestMapping("/get_whole_sql")
    @ResponseBody
    public String getWholeSql() {
        return manageClassService.getWholeSql();
    }

    @RequestMapping("/get_whole_sa_sql")
    @ResponseBody
    public String getWholeSaSql() {
        return manageClassService.getWholeSaSql();
    }

    @RequestMapping("/get_new_student_sql")
    @ResponseBody
    public String getNewStudentSql() {
        return payClassService.getNewStudentSql();
    }

    @RequestMapping("/get_old_student_sql")
    @ResponseBody
    public String getOldStudentSql() {
        return payClassService.getOldStudentSql();
    }

    @RequestMapping("/get_new_student_pay_cnt")
    @ResponseBody
    public String getNewStudentPayCnt() {
        return payClassService.getNewStudentPayCnt();
    }

    @RequestMapping("/get_old_student_pay_cnt")
    @ResponseBody
    public String getOldStudentPayCnt() {
        return payClassService.getOldStudentPayCnt();
    }

    @RequestMapping("/get_work_renew_cnt")
    @ResponseBody
    public ArrayList getWorkRenewCnt(){
        return workClassService.getWorkRenewCnt();
    }

    @RequestMapping("/get_book_choose_cost_class_sql")
    @ResponseBody
    public String getChooseBookWholeSql() {
        return bookClassService.getChooseBookWholeSql();
    }

    @RequestMapping("/get_work_student_message_sql")
    @ResponseBody
    public String getWorkStudentMessageSql() {
        return workClassService.getWorkStudentMessageSql();
    }

    @RequestMapping("/get_work_class_message_cnt")
    @ResponseBody
    public Long getWorkClassMessageCnt() {
        return workClassService.getWorkClassMessageCnt();
    }

    @RequestMapping("/get_work_refunds_cnt")
    @ResponseBody
    public ArrayList getWorkRefundsCnt(){
        return workClassService.getWorkRefundsCnt();
    }
    @RequestMapping("/get_work_refunds_sql")
    @ResponseBody
    public String getWorkRefundsSql(){
        return workClassService.getWorkRefundsSql();
    }
    @RequestMapping("/get_work_class_message_sql")
    @ResponseBody
    public String getWorkClassMessageSql() {
        return workClassService.getWorkClassMessageSql();
    }
    @RequestMapping("/get_work_renew_sql")
    @ResponseBody
    public String getWorkRenewSql() {
        return workClassService.getWorkRenewSql();
    }

    @RequestMapping("/get_work_lose_class_sql")
    @ResponseBody
    public String getWorkLoseClassSql(){return workClassService.getWorkLoseClassSql();}

    @RequestMapping("/get_work_lose_class_cnt")
    @ResponseBody
    public Long getWorkLoseClassCnt(){return workClassService.getWorkLoseClassCnt();}

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

    @RequestMapping(value = "/get_work_student_message/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getWorkStudentMessage(@PathVariable String data,HttpServletRequest request) throws ParseException{
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkStudentMessage> actions= workClassService.getWorkStudentMessage(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_work_student_recommend/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getWorkStudentRecommend(@PathVariable String data,HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkStudentRecommend> actions= workClassService.getWorkStudentRecommend(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_work_class_message/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getWorkClassMessage(@PathVariable String data,HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkClassMessage> actions= workClassService.getWorkClassMessage(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_lose_student_class/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getLoseStudentClass(@PathVariable String data,HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkLoseStudentClass> actions= workClassService.getWorkLoseStudentClass(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_student_lose_day/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ArrayList getStudentLoseDay(@PathVariable String data,HttpServletRequest request){
        ArrayList result= workClassService.getStudentLoseDay(data);
        return result;
    }

    @RequestMapping(value = "/get_lose_student_acoin_cnt/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse<WorkLoseStudentAcoin> getLoseStudentAcoinCnt(@PathVariable String data, HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkLoseStudentAcoin> actions= workClassService.getWorkLoseStudentAcoinCnt(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }


    @RequestMapping(value = "/get_lose_student_acoin/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getLoseStudentAcoin(@PathVariable String data,HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkLoseStudentAcoin> actions= workClassService.getWorkLoseStudentAcoin(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_work_renew/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getWorkRenew(@PathVariable String data,HttpServletRequest request){
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkRenew> actions= workClassService.getWorkRenew(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_work_refunds/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public DatatablesResponse getWorkRefunds(@PathVariable String data,HttpServletRequest request) {
        DatatablesCriterias criterias=DatatablesCriterias.getFromRequest(request);
        DataSet<WorkRefunds> actions= workClassService.getWorkRefunds(data,criterias);
        return DatatablesResponse.build(actions,criterias);
    }

    @RequestMapping(value = "/get_day_student_activity_chart/{data}")
    @ResponseBody
    @SuppressWarnings("uncheckede")
    public ArrayList getDayStudentActivityChart(@PathVariable String data){
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> actions = workClassService.getDayStudentActivityChart(data);
        for (Map.Entry<String, String> entry : actions.entrySet()) {
            Object[] array = new Object[2];
            String []cutTime=entry.getKey().split("\\.");
            array[0]=Long.parseLong(cutTime[0])*1000;
            if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                array[1] = Integer.parseInt(entry.getValue());
                list.add(array);
            }
        }
        return list;
    }
}