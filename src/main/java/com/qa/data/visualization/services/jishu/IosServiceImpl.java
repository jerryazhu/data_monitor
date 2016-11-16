package com.qa.data.visualization.services.jishu;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class IosServiceImpl implements IosService {
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "ios_app_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getIosApp() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select app_version as name,count(app_version) as count from ( select * from ABC360_IOS_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())- 3600*24*30) and app_version!='(null)') a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_IOS_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())- 3600*24*30) group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (app_version)");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "ios_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getIosSystem() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select os_version as name,count(os_version) as count from ( select * from ABC360_IOS_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())- 3600*24*30) and app_version!='(null)') a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_IOS_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())- 3600*24*30) group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                " GROUP BY (os_version)");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }


}
