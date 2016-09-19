package com.qa.data.visualization.services;

import com.qa.data.visualization.repositories.StuPCDailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StuPCDailyActivityServiceImpl implements StuPCDailyActivityService {
    @Autowired
    private StuPCDailyActivityRepository stuPCDailyActivityRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getDailyActivityMap() {
        LinkedHashMap<String, String> map=new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select FROM_UNIXTIME(login_time,'%Y-%m-%d') as time,count(distinct sid) as count from ebk_pc_stu_client group by FROM_UNIXTIME(login_time,'%Y-%m-%d') order by time asc");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

}
