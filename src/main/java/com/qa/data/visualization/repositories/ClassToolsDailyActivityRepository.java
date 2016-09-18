package com.qa.data.visualization.repositories;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassToolsDailyActivityRepository extends CrudRepository<ClassToolsDailyActivity, Integer> {

    @Query("select c from ClassToolsDailyActivity c where c.type = :type and c.time * 1000 >= 1325347200000")
    public List<ClassToolsDailyActivity> findByType(@Param("type") String type);
}
