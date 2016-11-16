package com.qa.data.visualization.controllers;

import com.qa.data.visualization.services.jishu.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/get_pay_daily_activity", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList findRegDailyActivity() throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> payDailyActivityMap = payService.getDailyActivityMap();
        for (Map.Entry<String, String> entry : payDailyActivityMap.entrySet()) {
            Object[] array = new Object[2];
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
            array[0] = dfm.parse(entry.getKey()).getTime();
            if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                BigDecimal mData = new BigDecimal(entry.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                array[1] = mData;
                list.add(array);
            }
        }
        return list;
    }

    @RequestMapping(value = "/get_pay_type_stock/{name}")
    @ResponseBody
    public ArrayList getPayTypeStock(@PathVariable String name) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        String now = String.valueOf(System.currentTimeMillis());
        LinkedHashMap<String, String> payTypeStock = payService.getPayTypeStock(name);
        for (Map.Entry<String, String> entry : payTypeStock.entrySet()) {
            Object[] array = new Object[2];
            if (entry.getKey() != null) {
                String[] cutTime = entry.getKey().split("\\.");
                String[] cutCnt = entry.getValue().split("\\.");
                if (cutTime[0].compareTo("1325347200000") > 0 && cutTime[0].compareTo(now) < 0) {
                    array[0] = Long.parseLong(cutTime[0]);
                    array[1] = Long.parseLong(cutCnt[0]);
                    list.add(array);
                }
            }
        }
        return list;
    }

    @RequestMapping(value = "/get_pay_type_time_stock/{name}")
    @ResponseBody
    public ArrayList getPayTypeTimeStock(@PathVariable String name) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        String now = String.valueOf(System.currentTimeMillis());
        LinkedHashMap<String, String> payTypeTimeStock = payService.getPayTypeTimeStock(name);
        for (Map.Entry<String, String> entry : payTypeTimeStock.entrySet()) {
            Object[] array = new Object[2];
            if (entry.getKey() != null) {
                String[] cutTime = entry.getKey().split("\\.");
                String[] cutCnt = entry.getValue().split("\\.");
                if (cutTime[0].compareTo("1325347200000") > 0 && cutTime[0].compareTo(now) < 0) {
                    array[0] = Long.parseLong(cutTime[0]);
                    array[1] = Long.parseLong(cutCnt[0]);
                    list.add(array);
                }
            }
        }
        return list;
    }

    @RequestMapping(value = "/get_pay_city_spread/{data}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public HashMap getPayCitySpread(@PathVariable String data) throws Exception {
        ArrayList message = payService.getPayCitySpread(data);
        HashMap cityMessage = new HashMap();
        cityMessage.put("cityNames", message.get(0));
        cityMessage.put("type0", message.get(1));
        cityMessage.put("type1", message.get(2));
        cityMessage.put("type2", message.get(3));
        cityMessage.put("type3", message.get(4));
        cityMessage.put("type4", message.get(5));
        return cityMessage;
    }

    @RequestMapping(value = "/get_pay_city/{time}")
    @ResponseBody
    public ArrayList getPayCityUrl(@PathVariable String time) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> payCity = payService.getPayCity(time);
        for (Map.Entry<String, String> entry : payCity.entrySet()) {
            Object[] array = new Object[2];
            array[0] = entry.getKey();
            array[1] = Integer.parseInt(entry.getValue());
            list.add(array);
        }
        return list;
    }

    @RequestMapping(value = "/get_pay_region_stock/{data}")
    @ResponseBody
    public ArrayList getPayRegionStock(@PathVariable String data) throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        String now = String.valueOf(System.currentTimeMillis());
        LinkedHashMap<String, String> payRegionStock = payService.getPayRegionStock(data);
        for (Map.Entry<String, String> entry : payRegionStock.entrySet()) {
            Object[] array = new Object[2];
            String[] cutTime = entry.getKey().split("\\.");
            String[] cutData = entry.getValue().split("\\.");
            if (cutTime[0].compareTo("1325347200000") > 0 && cutTime[0].compareTo(now) < 0) {
                array[0] = Long.parseLong(cutTime[0]);
                array[1] = Integer.parseInt(cutData[0]);
                list.add(array);
            }
        }
        return list;
    }
}
