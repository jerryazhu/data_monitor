package com.qa.data.visualization.services;

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
public class AndroidAppServiceImpl implements AndroidAppService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getAndroidApp() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select app_version as name,count(app_version) as count from ABC360_ANDROID_APP_DEVICE_TBL a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (app_version) ");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }
}
