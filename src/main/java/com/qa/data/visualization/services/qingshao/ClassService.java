package com.qa.data.visualization.services.qingshao;

import org.omg.CORBA.StringHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dykj on 2016/11/17.
 */
public interface ClassService {
    ArrayList getTeacherGroup();

    LinkedHashMap<String,String> getTeacherMessage(String data);
}
