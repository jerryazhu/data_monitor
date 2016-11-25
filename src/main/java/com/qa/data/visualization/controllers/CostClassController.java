package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.entities.qingshao.CostClass;
import com.qa.data.visualization.entities.qingshao.CostSaClass;
import com.qa.data.visualization.entities.qingshao.newStudent;
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
    public ArrayList getSaTeacherGroup(){
        return classService.getSaTeacherGroup();
    }
    @RequestMapping("/get_cost_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostClass> getCostClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostClass> dataSet = classService.getCostClass(data,criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }
    @RequestMapping("/get_cost_sa_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostSaClass> getCostSaClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostSaClass> dataSet = classService.getCostSaClass(data,criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }
    @RequestMapping("/get_new_student/{data}")
    @ResponseBody
    public DatatablesResponse<newStudent> getNewStudent(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<newStudent> dataSet = classService.getNewStudent(data,criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }
    @RequestMapping("/get_cost_class_cnt")
    @ResponseBody
    public Long getCostClassCnt(){
        return classService.getCostClassCnt();
    }

    @RequestMapping("/get_cost_sa_class_cnt")
    @ResponseBody
    public Long getCostSaClassCnt(){
        return classService.getCostSaClassCnt();
    }

    @RequestMapping("/get_new_student_cnt")
    @ResponseBody
    public Long getNewStudentCnt(){
        return classService.getNewStudentCnt();
    }

    @RequestMapping("/get_whole_sql")
    @ResponseBody
    public String getWholeSql(){
        return classService.getWholeSql();
    }

    @RequestMapping("/get_whole_sa_sql")
    @ResponseBody
    public String getWholeSaSql(){
        return classService.getWholeSaSql();
    }

    @RequestMapping("/get_new_student_sql")
    @ResponseBody
    public String getNewStudentSql(){
        return classService.getNewStudentSql();
    }

    @RequestMapping("/get_new_student_pay_cnt")
    @ResponseBody
    public String getNewStudentPayCnt(){
        return classService.getNewStudentPayCnt();
    }

}