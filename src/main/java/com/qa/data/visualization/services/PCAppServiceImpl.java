package com.qa.data.visualization.services;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/10/12.
 */
@Service
public class PCAppServiceImpl implements PCAppService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getPCApp() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select version as name,count(version) as count from ebk_pc_stu_client a inner JOIN\n" +
                "(select sid,max(login_time) as Login_time from ebk_pc_stu_client where version!='UnKnow version' group by sid) b\n" +
                "on a.login_time = b.login_time and a.sid = b.sid \n" +
                "GROUP BY (version)");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

}
