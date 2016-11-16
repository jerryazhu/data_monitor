package com.qa.data.visualization.services.jishu;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface RegService {
    LinkedHashMap<String, String> getDailyActivityMap();

    LinkedHashMap<String, String> getRegCity(String time);

    ArrayList getExperienceCity(String data);

    LinkedHashMap<String, String> getRegStock(String cityName);

    LinkedHashMap<String, String> getTrialStock(String nameType);
}
