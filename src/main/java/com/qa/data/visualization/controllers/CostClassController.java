package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.entities.qingshao.CostClass;
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
        ArrayList list = classService.getTeacherGroup();
        return list;
    }
    @RequestMapping("/get_cost_class/{data}")
    @ResponseBody
    public DatatablesResponse<CostClass> getCostClass(@PathVariable String data, HttpServletRequest request) throws ParseException {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<CostClass> dataSet = classService.getCostClass(data,criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }
    @RequestMapping("/get_cost_class_cnt")
    @ResponseBody
    public Long getCostClassCnt(){
        return classService.getCostClassCnt();
    }


}