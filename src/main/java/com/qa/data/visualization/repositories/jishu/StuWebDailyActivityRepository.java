package com.qa.data.visualization.repositories.jishu;

import com.qa.data.visualization.entities.jishu.StuWebDailyActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StuWebDailyActivityRepository extends CrudRepository<StuWebDailyActivity, Long> {

    @Query("select s from StuWebDailyActivity s where UNIX_TIMESTAMP(s.day) * 1000 >= 1325347200000")
    public List<StuWebDailyActivity> findWithMatchTime();
}
