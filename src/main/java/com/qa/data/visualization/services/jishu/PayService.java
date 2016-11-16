package com.qa.data.visualization.services.jishu;


import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface PayService {
    LinkedHashMap<String, String> getDailyActivityMap();

    LinkedHashMap<String, String> getPayTypeStock(String name);

    LinkedHashMap<String, String> getPayTypeTimeStock(String name);

    ArrayList getPayCitySpread(String data);

    LinkedHashMap<String, String> getPayRegionStock(String data);

    LinkedHashMap<String, String> getPayCity(String time);
}
