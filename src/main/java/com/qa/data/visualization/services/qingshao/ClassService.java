package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.AutoComplete;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/11/17.
 */
public interface ClassService {
    List<AutoComplete> getStudentAutoComplete(String query);

    List<AutoComplete> getTeacherAutoComplete(String query);

    ArrayList getTeacherGroup();

    LinkedHashMap<String, String> getTeacherMessage(String data);
}
