package com.qa.data.visualization.services;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PayServiceImpl implements PayService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getDailyActivityMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select concat(year,'-',month,'-',day) as time,sum(tmoney) from (\n" +
                "\tselect year,month,day,tmoney,orderid,IFNULL(si.study_aim,0) as study_aim \n" +
                "\tfrom ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                "\tleft join ebk_student_info si on pcr.sid = si.sid \n" +
                "\twhere (study_aim in (0,1,2,3,4) or study_aim is null)\n" +
                "\tand concat(year,'-',month,'-',day) > '2012-01-01'\n" +
                ") A\n" +
                "group by year,month,day");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

}
