package com.qa.data.visualization.repositories;

import com.qa.data.visualization.entities.ClassModeDailyActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassModeDailyActivityRepository extends CrudRepository<ClassModeDailyActivity, Long> {
}
