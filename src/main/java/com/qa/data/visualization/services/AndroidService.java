package com.qa.data.visualization.services;

import java.util.LinkedHashMap;

/**
 * Created by dykj on 2016/10/24.
 */
public interface AndroidService {
    LinkedHashMap<String,String> getAndroidApp();
    LinkedHashMap <String,String> getAndroidSystem();
}
