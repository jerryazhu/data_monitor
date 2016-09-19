package com.qa.data.visualization.services;


import com.qa.data.visualization.entities.ClassToolsDailyActivity;

import java.util.List;

public interface ClassToolsDailyActivityService {
    Iterable<ClassToolsDailyActivity> listAllDailyActivity();

    ClassToolsDailyActivity getDailyActivityById(Long id);

    List<ClassToolsDailyActivity> getDailyActivitiesByType(String type);

    ClassToolsDailyActivity saveDailyActivity(ClassToolsDailyActivity dailyActivity);

    void deleteDailyActivity(Long id);

}
