package com.qa.data.visualization.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/10/10.
 */
public class IosAppServiceImpl implements IosAppService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getIosApp() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select s.os_version as name,count(s.os_version) as count from (select max(time),uid,os_version from ABC360_IOS_APP_DEVICE_TBL group by uid ) s group by s.os_version ");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }
}
