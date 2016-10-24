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
public class RegServiceImpl implements RegService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "reg_cache", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, String> getDailyActivityMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select concat(year,'-',month,'-',day) as time,sum(cnt) as count from ABC360_REG_CITY_DAILY_TBL\n" +
                "where concat(year,'-',month,'-',day) > '2012-01-01'\n" +
                "group by year,month,day");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getRegCity(String time) {
        String[] cutTime = time.split("---");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select city,sum(cnt) as cnt from ABC360_REG_CITY_DAILY_TBL \n" +
                "where UNIX_TIMESTAMP(" + "'" + cutTime[0] + "'" + ")<=UNIX_TIMESTAMP(concat(year,'-',month,'-',day)) and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP(" + "'" + cutTime[1] + "'" + ") and city!='' \n" +
                "group by city order by cnt desc limit 20");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "experience_city", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getExperienceCity(String data) {
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
            q = entityManager.createNativeQuery("select city,count(A.study_aim) as cnt from(\n" +
                    "select rcr.city,IFNULL(si.study_aim,0) as study_aim\n" +
                    "from ABC360_REG_CITY_RECORD_TBL rcr\n" +
                    "left join ebk_student_info si on rcr.sid=si.sid\n" +
                    "left join ebk_students s on si.sid=s.id\n" +
                    "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                    "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and s.status=0\n" +
                    "and study_aim=" + type + "\n" +
                    ")A\n" +
                    "group by city,study_aim\n" +
                    "order by cnt DESC limit 20");
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
            q = entityManager.createNativeQuery("select city,count(A.study_aim) as cnt from(\n" +
                    "select rcr.city,IFNULL(si.study_aim,0) as study_aim\n" +
                    "from ABC360_REG_CITY_RECORD_TBL rcr\n" +
                    "left join ebk_student_info si on rcr.sid=si.sid\n" +
                    "left join ebk_students s on si.sid=s.id\n" +
                    "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                    "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and s.status=0\n" +
                    ")A\n" +
                    "group by city\n" +
                    "order by cnt DESC limit 20");
            List<Object[]> list = q.getResultList();
            for (Object[] result : list) {
                cityName.add(result[0]);
            }
            for (Object aCityName : cityName) {
                q = entityManager.createNativeQuery("select city,A.study_aim,count(A.study_aim) as cnt from(\n" +
                        "select rcr.city,IFNULL(si.study_aim,0) as study_aim\n" +
                        "from ABC360_REG_CITY_RECORD_TBL rcr\n" +
                        "left join ebk_student_info si on rcr.sid=si.sid\n" +
                        "left join ebk_students s on si.sid=s.id\n" +
                        "where UNIX_TIMESTAMP(concat(year,'-',month,'-',day))>=UNIX_TIMESTAMP('" + cutData[0] + "')\n" +
                        "and UNIX_TIMESTAMP(concat(year,'-',month,'-',day))<=UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                        "and s.status=0\n" +
                        "and rcr.city='" + aCityName + "'\n" +
                        ")A\n" +
                        "group by city,study_aim\n" +
                        "order by cnt DESC limit 20");
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

        if (number0.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number0.add("0");
            }
        }
        if (number1.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number1.add("0");
            }
        }
        if (number2.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number2.add("0");
            }
        }
        if (number3.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number3.add("0");
            }
        }
        if (number4.size() == 0) {
            for (int i = 0; i < 20; i++) {
                number4.add("0");
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
}
