package com.qa.data.visualization.services;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/10/9.
 */
@Service
public class AndroidSystemServiceImpl implements AndroidSystemService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "android_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getAndroidSystem() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select os_version as name,count(os_version) as count from ( select * from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*10*1000)) a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*10*1000) group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (os_version)");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

}
