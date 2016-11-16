package com.qa.data.visualization.services.jishu;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class StuPCDailyActivityServiceImpl implements StuPCDailyActivityService {
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pc_daily_activity_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getDailyActivityMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select FROM_UNIXTIME(login_time,'%Y-%m-%d') as time,count(distinct sid) as count from ebk_pc_stu_client group by FROM_UNIXTIME(login_time,'%Y-%m-%d') order by time asc");
        List<Object[]> list = q.getResultList();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        String today = dfm.format(new Date());
        for (Object[] result : list) {
            if (!result[0].toString().equals(today)) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

}
