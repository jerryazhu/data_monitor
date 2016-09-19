package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import com.qa.data.visualization.repositories.ClassToolsDailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassToolsDailyActivityServiceImpl implements ClassToolsDailyActivityService {
    @Autowired
    private ClassToolsDailyActivityRepository classToolsDailyActivityRepository;

    @Override
    public Iterable<ClassToolsDailyActivity> listAllDailyActivity() {
        return classToolsDailyActivityRepository.findAll();
    }

    @Override
    public ClassToolsDailyActivity getDailyActivityById(Long id) {
        return classToolsDailyActivityRepository.findOne(id);
    }

    @Override
    public List<ClassToolsDailyActivity> getDailyActivitiesByType(String type) {
        return classToolsDailyActivityRepository.findByType(type);
    }

    @Override
    public ClassToolsDailyActivity saveDailyActivity(ClassToolsDailyActivity dailyActivity) {
        return classToolsDailyActivityRepository.save(dailyActivity);
    }

    @Override
    public void deleteDailyActivity(Long id) {
        classToolsDailyActivityRepository.delete(id);
    }
}
