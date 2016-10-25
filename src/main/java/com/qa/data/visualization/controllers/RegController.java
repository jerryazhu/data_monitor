package com.qa.data.visualization.controllers;

import com.qa.data.visualization.services.RegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/reg")
public class RegController {

    @Autowired
    private RegService regService;

    @RequestMapping(value = "/get_reg_daily_activity", method = RequestMethod.GET)
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

    @RequestMapping(value = "/get_reg_city/{timeRange}", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList getRegCity(@PathVariable String timeRange) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> regCity = regService.getRegCity(timeRange);
        for (Map.Entry<String, String> entry : regCity.entrySet()) {
            Object[] array = new Object[2];
            String key = entry.getKey();
            array[0] = key;
            array[1] = Integer.parseInt(entry.getValue());
            list.add(array);
        }
        return list;
    }

    @RequestMapping(value = "/get_experience_city/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public HashMap getExperienceCity(@PathVariable String data) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        ArrayList message = regService.getExperienceCity(data);
        HashMap cityMessage = new HashMap();
        cityMessage.put("cityNames", message.get(0));
        cityMessage.put("type0", message.get(1));
        cityMessage.put("type1", message.get(2));
        cityMessage.put("type2", message.get(3));
        cityMessage.put("type3", message.get(4));
        cityMessage.put("type4", message.get(5));
        return cityMessage;
    }

    @RequestMapping(value = "/get_reg_stock/{name}")
    @ResponseBody
    public ArrayList getRegStock(@PathVariable String name) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> regStock = regService.getRegStock(name);
        for (Map.Entry<String, String> entry : regStock.entrySet()) {
            Object[] array = new Object[2];
            array[0] = Long.parseLong(entry.getKey());
            array[1] = Long.parseLong(entry.getValue());
            list.add(array);
        }
        return list;
    }

    @RequestMapping(value = "/get_trial_stock/{nameType}")
    @ResponseBody
    public ArrayList getTrialStock(@PathVariable String nameType) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> trialStock = regService.getTrialStock(nameType);
        for (Map.Entry<String, String> entry : trialStock.entrySet()) {
            Object[] array = new Object[2];
            array[0] = Long.parseLong(entry.getKey());
            array[1] = Long.parseLong(entry.getValue());
            list.add(array);
        }
        return list;
    }
}
