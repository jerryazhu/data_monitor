package com.qa.data.visualization.services.jishu;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/10/24.
 */
@Service
public class AndroidServiceImpl implements AndroidService {
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "android_app_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getAndroidApp() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String s = String.format("select app_version as name,count(app_version) as count from ( select * from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000)) a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000) group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (app_version) ");
        Query q = entityManager.createNativeQuery(s);
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "android_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getAndroidSystem() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String s = String.format("select os_version as name,count(os_version) as count from ( select * from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000)) a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000) group by uid,model) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (os_version)");
        Query q = entityManager.createNativeQuery(s);
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }
}
