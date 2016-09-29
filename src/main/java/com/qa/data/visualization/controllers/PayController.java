package com.qa.data.visualization.controllers;

import com.qa.data.visualization.services.PayService;
import com.qa.data.visualization.services.RegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/get_pay_daily_activity")
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

}
