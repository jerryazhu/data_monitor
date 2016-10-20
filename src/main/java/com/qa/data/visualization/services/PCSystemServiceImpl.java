package com.qa.data.visualization.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/10/11.
 */
@Service
public class PCSystemServiceImpl implements PCSystemService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pc_os_version_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getPCSystem() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select os as name,count(os) as count from (select * from ebk_pc_stu_client where os!='Unknow os' and login_time > (UNIX_TIMESTAMP(now())- 3600*24*10)) a inner JOIN\n" +
                "(select sid,max(login_time) as Login_time from ebk_pc_stu_client where os!='Unknow os' and login_time > (UNIX_TIMESTAMP(now())- 3600*24*10) group by sid) b\n" +
                "on a.login_time = b.login_time and a.sid = b.sid \n" +
                "GROUP BY (os)");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }
}
