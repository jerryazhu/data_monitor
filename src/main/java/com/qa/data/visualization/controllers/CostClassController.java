package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.services.qingshao.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public List<AutoComplete> autocomplete(@RequestParam(value = "query", required = true) String query, HttpServletRequest request) {
        return classService.getStudentAutoComplete(query);
    }

    @RequestMapping("/get_teacher_group")
    @ResponseBody
    public ArrayList getTeacherGroup() {
        ArrayList list = classService.getTeacherGroup();
        return list;
    }

    @RequestMapping("/get_teacher_message/{data}")
    @ResponseBody
    public ArrayList getTeacherMessage(@PathVariable String data) {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> teacherMessage = classService.getTeacherMessage(data);
        for (Map.Entry<String, String> entry : teacherMessage.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", entry.getKey());
            map.put("data", entry.getValue());
            list.add(map);
        }
        return list;
    }

}