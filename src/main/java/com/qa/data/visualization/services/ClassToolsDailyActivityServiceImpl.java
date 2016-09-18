package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import com.qa.data.visualization.repositories.ClassToolsDailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassToolsDailyActivityServiceImpl implements ClassToolsDailyActivityService {
    private ClassToolsDailyActivityRepository classToolsDailyActivityRepository;

    @Autowired
    public void setClassToolsDailyActivityRepository(ClassToolsDailyActivityRepository classToolsDailyActivityRepository) {
        this.classToolsDailyActivityRepository = classToolsDailyActivityRepository;
    }

    @Override
    public Iterable<ClassToolsDailyActivity> listAllDailyActivity() {
        return classToolsDailyActivityRepository.findAll();
    }

    @Override
    public ClassToolsDailyActivity getDailyActivityById(Integer id) {
        return classToolsDailyActivityRepository.findOne(id);
    }

    public List<ClassToolsDailyActivity> getDailyActivitiesByType(String type) {
        return classToolsDailyActivityRepository.findByType(type);
    }

    @Override
    public ClassToolsDailyActivity saveDailyActivity(ClassToolsDailyActivity dailyActivity) {
        return classToolsDailyActivityRepository.save(dailyActivity);
    }

    @Override
    public void deleteDailyActivity(Integer id) {
        classToolsDailyActivityRepository.delete(id);
    }
}
