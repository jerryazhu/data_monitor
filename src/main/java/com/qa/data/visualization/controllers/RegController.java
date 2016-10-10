package com.qa.data.visualization.controllers;

import com.qa.data.visualization.services.RegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/reg")
public class RegController {

    @Autowired
    private RegService regService;

    @RequestMapping(value = "/get_reg_daily_activity", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList findRegDailyActivity() throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> regDailyActivityMap = regService.getDailyActivityMap();
        for (Map.Entry<String, String> entry : regDailyActivityMap.entrySet()) {
            Object[] array = new Object[2];
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
            array[0] = dfm.parse(entry.getKey()).getTime();
            if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                array[1] = Integer.parseInt(entry.getValue());
                list.add(array);
            }
        }
        return list;
    }

}
