package com.qa.data.visualization.services.jishu;


import com.qa.data.visualization.entities.jishu.ClassToolsDailyActivity;

import java.util.List;

public interface ClassToolsDailyActivityService {
    Iterable<ClassToolsDailyActivity> listAllDailyActivity();

    ClassToolsDailyActivity getDailyActivityById(Long id);

    List<ClassToolsDailyActivity> getDailyActivitiesByType(String type);

    ClassToolsDailyActivity saveDailyActivity(ClassToolsDailyActivity dailyActivity);

    void deleteDailyActivity(Long id);

}
