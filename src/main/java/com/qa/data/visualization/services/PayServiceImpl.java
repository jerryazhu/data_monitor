package com.qa.data.visualization.services;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_type", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getPayTypeStock(String name) {
        String sqlType = null;
        if (name.equals("all")) {
            sqlType = "study_aim in(0,1,2,3,4)";
        } else {
            sqlType = "study_aim=" + name;
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select UNIX_TIMESTAMP(CONVERT_TZ(concat(year,'-',month,'-',day),'+00:00','SYSTEM'))*1000 as time,sum(tmoney) from\n" +
                "(select year,month,day,tmoney,orderid,IFNULL(si.study_aim,0) as study_aim\n" +
                "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                "left join ebk_student_info si on pcr.sid=si.sid\n" +
                "where " + sqlType + ")A\n" +
                "group by year,month,day");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            if (result[0] != null) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_type_time", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getPayTypeTimeStock(String name) {
        String sqlType = null;
        if (name.equals("all")) {
            sqlType = "study_aim in(0,1,2,3,4)";
        } else {
            sqlType = "study_aim=" + name;
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select UNIX_TIMESTAMP(CONVERT_TZ(concat(year,'-',month,'-',day),'+00:00','SYSTEM'))*1000 as time, count(orderid) from\n" +
                "(select year,month,day,tmoney,orderid,IFNULL(si.study_aim,0) as study_aim\n" +
                "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                "left join ebk_student_info si on pcr.sid=si.sid\n" +
                "where " + sqlType + ")A\n" +
                "group by year,month,day");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            if (result[0] != null) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_city_spread", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getPayCitySpread(String data) {
        ArrayList cityName = new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        ArrayList number2 = new ArrayList();
        ArrayList number3 = new ArrayList();
        ArrayList number4 = new ArrayList();
        String[] cutData = data.split("---");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String type = cutData[2];
        Query q = null;
        if (!type.equals("all")) {
            q = entityManager.createNativeQuery("select city,sum(A.tmoney) as money from(\n" +
                    "select pcr.city,tmoney,IFNULL(si.study_aim,0) as study_aim\n" +
                    "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                    "left join ebk_student_info si on pcr.sid=si.sid\n" +
                    "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                    "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and study_aim=" + type + "\n" +
                    ")A\n" +
                    "group by city,study_aim\n" +
                    "order by money DESC limit 20");
            List<Object[]> list = q.getResultList();
            for (Object[] result : list) {
                cityName.add(result[0]);
            }
            for (Object[] result : list) {
                switch (type) {
                    case "0":
                        number0.add(result[1]);
                        break;
                    case "1":
                        number1.add(result[1]);
                        break;
                    case "2":
                        number2.add(result[1]);
                        break;
                    case "3":
                        number3.add(result[1]);
                        break;
                    case "4":
                        number4.add(result[1]);
                        break;
                }
            }
        } else {
            q = entityManager.createNativeQuery("select city,sum(A.tmoney) as money from(\n" +
                    "select pcr.city,tmoney,IFNULL(si.study_aim,0) as study_aim\n" +
                    "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                    "left join ebk_student_info si on pcr.sid=si.sid\n" +
                    "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                    "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    ")A\n" +
                    "group by city\n" +
                    "order by money DESC limit 20");
            List<Object[]> list = q.getResultList();
            for (Object[] result : list) {
                cityName.add(result[0]);
            }
            for (Object aCityName : cityName) {
                q = entityManager.createNativeQuery("select city,A.study_aim,sum(A.tmoney) as money  from(\n" +
                        "select pcr.city,tmoney,IFNULL(si.study_aim,0) as study_aim\n" +
                        "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                        "left join ebk_student_info si on pcr.sid=si.sid\n" +
                        "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                        "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                        "and pcr.city='" + aCityName + "'\n" +
                        ")A\n" +
                        "group by city,study_aim\n" +
                        "order by money DESC limit 20");
                List<Object[]> cityMessage = q.getResultList();
                for (Object[] result : cityMessage) {
                    switch (result[1].toString()) {
                        case "0":
                            number0.add(result[2]);
                            break;
                        case "1":
                            number1.add(result[2]);
                            break;
                        case "2":
                            number2.add(result[2]);
                            break;
                        case "3":
                            number3.add(result[2]);
                            break;
                        case "4":
                            number4.add(result[2]);
                            break;

                    }
                }
            }
        }
        int zero = 0;
        if (number0.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number0.add(zero);
            }
        }
        if (number1.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number1.add(zero);
            }
        }
        if (number2.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number2.add(zero);
            }
        }
        if (number3.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number3.add(zero);
            }
        }
        if (number4.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number4.add(zero);
            }
        }
        ArrayList message = new ArrayList();
        message.add(cityName);
        message.add(number0);
        message.add(number1);
        message.add(number2);
        message.add(number3);
        message.add(number4);
        return message;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_region", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getPayRegionStock(String data) {
        String[] cutData = data.split("---");
        String typeSql = null;
        if (cutData[1].equals("all")) {
            typeSql = "study_aim in(0,1,2,3,4)";
        } else {
            typeSql = "study_aim=" + cutData[1];
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select UNIX_TIMESTAMP(CONVERT_TZ(concat(year,'-',month,'-',day),'+00:00','SYSTEM'))*1000 as time, sum(tmoney) from\n" +
                "(select year,month,day,tmoney,orderid,IFNULL(si.study_aim,0) as study_aim\n" +
                "from ABC360_PAY_CITY_RECORD_TBL pcr\n" +
                "left join ebk_student_info si on pcr.sid=si.sid\n" +
                "where " + typeSql + "\n" +
                "and city='" + cutData[0] + "')A\n" +
                "group by year,month,day");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            if (result[0] != null) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "pay_city", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getPayCity(String time) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String[] cutTime = time.split("---");
        Query q = entityManager.createNativeQuery("select city,sum(tmoney) as money from ABC360_PAY_CITY_DAILY_TBL \n" +
                "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day)) >= UNIX_TIMESTAMP('" + cutTime[0] + "') and UNIX_TIMESTAMP(concat(year,'-',month,'-',day)) <= UNIX_TIMESTAMP('" + cutTime[1] + "') \n" +
                "and city!='' group by city order by money DESC  limit 20;");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }
}
