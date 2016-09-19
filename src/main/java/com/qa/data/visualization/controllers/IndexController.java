package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import com.qa.data.visualization.entities.StuMobileDailyActivity;
import com.qa.data.visualization.entities.StuTerminal;
import com.qa.data.visualization.entities.StuWebDailyActivity;
import com.qa.data.visualization.repositories.*;
import com.qa.data.visualization.services.ClassToolsDailyActivityService;
import com.qa.data.visualization.services.StuPCDailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private ClassToolsDailyActivityService classToolsDailyActivityService;
    @Autowired
    private LastErrorRepository lastErrorRepository;
    @Autowired
    private LastDebugRepository lastDebugRepository;
    @Autowired
    private StuWebDailyActivityRepository stuWebDailyActiityRepository;
    @Autowired
    private StuPCDailyActivityService stuPCDailyActivityService;
    @Autowired
    private StuMobileDailyActivityRepository stuMobileDailyActivityRepository;
    @Autowired
    private StuTerminalRepository stuTerminalRepository;

    @RequestMapping("/")
    String index(Model model) {
        long lastError = lastErrorRepository.count();
        model.addAttribute("lastError", lastError);
        long lastDebug = lastDebugRepository.count();
        model.addAttribute("lastDebug", lastDebug);
        return "index";
    }

    @RequestMapping("class_tools_daily_activity/{type}")
    @ResponseBody
    public ArrayList getClassToolDailyActivityByType(@PathVariable String type) {
        ArrayList<Object> list = new ArrayList<Object>();
        List<ClassToolsDailyActivity> classToolsDailyActivityList = classToolsDailyActivityService.getDailyActivitiesByType(type);
        for (ClassToolsDailyActivity da : classToolsDailyActivityList) {
            Object[] array = new Object[2];
            array[0] = Long.parseLong(da.getTime()) * 1000 + 3600 * 23 * 1000;
            array[1] = da.getCount();
            list.add(array);
        }
        return list;
    }

    @RequestMapping("student_web_daily_activity/{type}")
    @ResponseBody
    public ArrayList getStuWebDailyActivityByType(@PathVariable String type) throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        if (type.equals("Web")) {
            List<StuWebDailyActivity> stuWebDailyActivityList = stuWebDailyActiityRepository.findWithMatchTime();
            for (StuWebDailyActivity da : stuWebDailyActivityList) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getDay()).getTime() + 3600 * 23 * 1000;
                array[1] = da.getCount();
                list.add(array);
            }
        } else if (type.equals("ClassPlat")) {
            LinkedHashMap<String, String> stuPCDailyActivityMap = stuPCDailyActivityService.getDailyActivityMap();
            for (Map.Entry<String, String> entry : stuPCDailyActivityMap.entrySet()) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(entry.getKey()).getTime() + 3600 * 23 * 1000;
                if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                    array[1] = Integer.parseInt(entry.getValue());
                    list.add(array);
                }
            }
        } else if (type.equals("Android")) {
            List<StuMobileDailyActivity> stuMoblieDailyActivityList = stuMobileDailyActivityRepository.findByType(2);
            for (StuMobileDailyActivity da : stuMoblieDailyActivityList) {
                Object[] array = new Object[2];
                array[0] = da.getDay() * 1000 + 3600 * 23 * 1000;
                array[1] = da.getCount();
                list.add(array);
            }
        } else {
            List<StuMobileDailyActivity> stuMoblieDailyActivityList = stuMobileDailyActivityRepository.findByType(3);
            for (StuMobileDailyActivity da : stuMoblieDailyActivityList) {
                Object[] array = new Object[2];
                array[0] = da.getDay() * 1000 + 3600 * 23 * 1000;
                array[1] = da.getCount();
                list.add(array);
            }
        }
        return list;
    }

    @RequestMapping("get_student_terminal")
    @ResponseBody
    public ArrayList getStuTerminal() {
        ArrayList<Object> list = new ArrayList<Object>();
        Iterable<StuTerminal> stuTerminalList = stuTerminalRepository.findAll();
        for (StuTerminal st : stuTerminalList) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("name",st.getTerminal());
            map.put("count",st.getCnt());
            list.add(map);
        }
        return list;
    }
}
