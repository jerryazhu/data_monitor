package com.qa.data.visualization.repositories.jishu;

import com.qa.data.visualization.entities.jishu.StuMobileDailyActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StuMobileDailyActivityRepository extends CrudRepository<StuMobileDailyActivity, Long> {
    @Query("select s from StuMobileDailyActivity s where s.type = :type")
    public List<StuMobileDailyActivity> findByType(@Param("type") Integer type);
}
