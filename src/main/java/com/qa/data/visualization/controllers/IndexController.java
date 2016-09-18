package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import com.qa.data.visualization.services.ClassToolsDailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private ClassToolsDailyActivityService classToolsDailyActivityService;

    @Autowired
    public void setClassToolsDailyActivityService(ClassToolsDailyActivityService classToolsDailyActivityService) {
        this.classToolsDailyActivityService = classToolsDailyActivityService;
    }

    @RequestMapping("/")
    String index(){
        return "index";
    }

    @RequestMapping("class_tools_daily_activity/{type}")
    @ResponseBody
    public ArrayList getDailyActivityByType(@PathVariable String type){
        ArrayList<Object> list = new ArrayList<Object>();
        List<ClassToolsDailyActivity> ClassToolsDailyActivityList = classToolsDailyActivityService.getDailyActivitiesByType(type);
        for (ClassToolsDailyActivity da:ClassToolsDailyActivityList) {
            Object[] array = new Object[2];
            array[0]=Long.parseLong(da.getTime())*1000;
            array[1]=da.getCount();
            list.add(array);
        }
        return list;
    }
}
