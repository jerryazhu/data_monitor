package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.CostClass;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dykj on 2016/11/30.
 */
@Service
public class BookClassServiceImpl implements BookClassService {
    private Long chooseBookCostClassCnt;
    private String chooseBookWholeSql;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @RequestMapping
    public LinkedHashMap<String, String> getBook(String data) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String[] cutTime = data.split("---");
        Query q = entityManager.createNativeQuery("select empt.name,count(ecr.id) as cnt from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                "where esi.study_aim=1 and ecr.stype!=''\n" +
                "and ecr.status=3\n" +
                "and ecr.begin_time between UNIX_TIMESTAMP('" + cutTime[0] + "') and UNIX_TIMESTAMP('" + cutTime[1] + "')\n" +
                "group by empt.name \n" +
                "order by cnt desc");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public ArrayList getBookMonthChoose(String data) {
        ArrayList bookName = new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        String[] cutTime = data.split("---");
        LinkedHashMap<String, String> getBook = getBook(data);
        for (Map.Entry<String, String> entry : getBook.entrySet()) {
            if (entry.getKey().contains("'")) {
                bookName.add(entry.getKey().replace("'", "\'"));
            } else {
                bookName.add(entry.getKey());
            }
        }
        for (Object aBookName : bookName) {
            String s = String.format("select empt.name,count(ecr.id)from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!=''\n" +
                    "and ecr.status=3\n" +
                    "and ecr.begin_time between UNIX_TIMESTAMP('%s') and UNIX_TIMESTAMP('%s')\n" +
                    "and empt.name=\"%s\"" +
                    "group by empt.name", cutTime[0], cutTime[1], aBookName.toString());
            Query q = entityManager.createNativeQuery(s);
            List<Object[]> list1 = q.getResultList();
            if (list1.size() < 1) {
                number0.add(0);
            } else {
                for (Object[] result : list1) {
                    number0.add(result[1]);
                }
            }
            q = entityManager.createNativeQuery("select re.name,count(re.name) FROM\n" +
                    "(select empt.name as name,ecr.sid from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!=''\n" +
                    "and ecr.status=3\n" +
                    "and ecr.begin_time between UNIX_TIMESTAMP('" + cutTime[0] + "') and UNIX_TIMESTAMP('" + cutTime[1] + "')\n" +
                    "and empt.name=\"" + aBookName + "\"\n" +
                    "GROUP BY ecr.sid) re");
            List<Object[]> list2 = q.getResultList();
            if (list2.size() < 1) {
                number1.add(0);
            } else {
                for (Object[] result : list2) {
                    number1.add(result[1]);
                }
            }
        }
        ArrayList message = new ArrayList();
        message.add(bookName);
        message.add(number0);
        message.add(number1);
        return message;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getBookChooseClassStock(String data) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select ecr.begin_time*1000,count(ecr.id)from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                "where esi.study_aim=1 and ecr.stype!=''\n" +
                "and ecr.status=3\n" +
                "and empt.name=\"" + data + "\"\n" +
                "group by ecr.begin_time");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            if (result[0] != null) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public ArrayList getStudentLevels() {
        ArrayList resultList = new ArrayList();
        Query q = entityManager.createNativeQuery("select concat(es.level,es.sub_level) as levels from ebk_students es\n" +
                "LEFT JOIN ebk_class_records ecr on es.id=ecr.sid\n" +
                "where es.level is not null and es.sub_level is not null\n" +
                "GROUP BY levels\n");
        List list = q.getResultList();
        for (Object aList : list) {
            resultList.add(aList.toString());
        }
        return resultList;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public ArrayList getBookRankChooseClass(String data) {
        ArrayList xName = new ArrayList();
        ArrayList levels = new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        ArrayList message = new ArrayList();
        String[] cutData = data.split("---");
        if (cutData[3].equals("all")) {
            levels = getStudentLevels();
        } else {
            levels.add(cutData[3]);
        }
        for (Object aLevels : levels) {
            Query q = entityManager.createNativeQuery("select empt.name as name,count(ecr.id) from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!=''\n" +
                    "and ecr.status=3\n" +
                    "and concat(es.level,es.sub_level)='" + aLevels + "'\n" +
                    "and ecr.begin_time between UNIX_TIMESTAMP('" + cutData[0] + "') and UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and empt.name=\"" + cutData[2] + "\" \n" +
                    "GROUP BY empt.name");
            List<Object[]> list1 = q.getResultList();
            if (list1.size() < 1) {
                number0.add(0);
            } else {
                for (Object[] result : list1) {
                    number0.add(result[1]);
                }
            }
            q = entityManager.createNativeQuery("select re.name,count(re.name) FROM\n" +
                    "(select empt.name as name,ecr.sid from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!=''\n" +
                    "and ecr.status=3\n" +
                    "and concat(es.level,es.sub_level)='" + aLevels + "'\n" +
                    "and ecr.begin_time between UNIX_TIMESTAMP('" + cutData[0] + "') and UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and empt.name=\"" + cutData[2] + "\"\n" +
                    "GROUP BY ecr.sid) re");
            List<Object[]> list2 = q.getResultList();
            if (list2.size() < 1) {
                number1.add(0);
            } else {
                for (Object[] result : list2) {
                    number1.add(result[1]);
                }
            }
        }
        message.add(levels);
        message.add(number0);
        message.add(number1);
        return message;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public ArrayList getBookRankAge(String data) {
        ArrayList xName = new ArrayList();
        ArrayList levels = new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        ArrayList number2 = new ArrayList();
        ArrayList number3 = new ArrayList();
        ArrayList number4 = new ArrayList();
        ArrayList number5 = new ArrayList();
        ArrayList number6 = new ArrayList();
        ArrayList number7 = new ArrayList();
        ArrayList number8 = new ArrayList();
        ArrayList number9 = new ArrayList();
        ArrayList message = new ArrayList();
        String[] cutData = data.split("---");
        if (cutData[3].equals("all")) {
            levels = getStudentLevels();
        } else {
            levels.add(cutData[3]);
        }
        for (Object aLevels : levels) {
            for (int i = 0; i < 9; i++) {
                Query q = entityManager.createNativeQuery("select re.name,count(re.name) FROM\n" +
                        "(select empt.name as name,ecr.sid from ebk_class_records ecr\n" +
                        "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                        "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                        "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                        "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                        "where esi.study_aim=1 and ecr.stype!=''\n" +
                        "and ecr.status=3\n" +
                        "and concat(es.level,es.sub_level)='" + aLevels + "'\n" +
                        "and ecr.begin_time between UNIX_TIMESTAMP('" + cutData[0] + "') and UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                        "and empt.name=\"" + cutData[2] + "\"\n" +
                        "and esi.age_duration=" + i + "\n" +
                        "GROUP BY ecr.sid) re");
                List<Object[]> list1 = q.getResultList();
                if (list1.size() < 1) {
                    switch (i) {
                        case 0:
                            number0.add(0);
                            break;
                        case 1:
                            number1.add(0);
                            break;
                        case 2:
                            number2.add(0);
                            break;
                        case 3:
                            number3.add(0);
                            break;
                        case 4:
                            number4.add(0);
                            break;
                        case 5:
                            number5.add(0);
                            break;
                        case 6:
                            number6.add(0);
                            break;
                        case 7:
                            number7.add(0);
                            break;
                        case 8:
                            number8.add(0);
                            break;
                    }
                } else {
                    for (Object[] result : list1) {
                        switch (i) {
                            case 0:
                                number0.add(result[1]);
                                break;
                            case 1:
                                number1.add(result[1]);
                                break;
                            case 2:
                                number2.add(result[1]);
                                break;
                            case 3:
                                number3.add(result[1]);
                                break;
                            case 4:
                                number4.add(result[1]);
                                break;
                            case 5:
                                number5.add(result[1]);
                                break;
                            case 6:
                                number6.add(result[1]);
                                break;
                            case 7:
                                number7.add(result[1]);
                                break;
                            case 8:
                                number8.add(result[1]);
                                break;
                        }
                    }
                }
            }
            Query q = entityManager.createNativeQuery("select re.name,count(re.name) FROM\n" +
                    "(select empt.name as name,ecr.sid from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!=''\n" +
                    "and ecr.status=3\n" +
                    "and concat(es.level,es.sub_level)='" + aLevels + "'\n" +
                    "and ecr.begin_time between UNIX_TIMESTAMP('" + cutData[0] + "') and UNIX_TIMESTAMP('" + cutData[1] + "')\n" +
                    "and empt.name=\"" + cutData[2] + "\"\n" +
                    "and esi.age_duration>8 \n" +
                    "GROUP BY ecr.sid) re");
            List<Object[]> list2 = q.getResultList();
            if (list2.size() < 1) {
                number9.add(0);
            } else {
                for (Object[] result : list2) {
                    number9.add(result[1]);
                }
            }
        }
        message.add(levels);
        message.add(number0);
        message.add(number1);
        message.add(number2);
        message.add(number3);
        message.add(number4);
        message.add(number5);
        message.add(number6);
        message.add(number7);
        message.add(number8);
        message.add(number9);
        return message;

    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getBookRankChooseClassCompare(String data) {
        String[] cutData = data.split("---");
        String sql;
        if (cutData[cutData.length - 1].equals("all")) {
            sql = "concat(es.level,es.sub_level) in( '0', '1', '2', '3A', '3B', '4A', '4B', '4C', '5A', '5B', '5C', '6A', '6B', '6C', '7A', '7B', '7C', '8A', '8B')";
        } else {
            sql = "concat(es.level,es.sub_level)='" + cutData[cutData.length - 1] + "'";
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Query q = entityManager.createNativeQuery("select ecr.begin_time*1000,count(ecr.id)from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                "where esi.study_aim=1 and ecr.stype!=''\n" +
                "and ecr.status=3\n" +
                "and empt.name=\"" + cutData[0] + "\"\n" +
                "and " + sql + "\n" +
                "group by ecr.begin_time");
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
    public DataSet<CostClass> getBookChooseCostClass(String data, DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime();
        long yesterdayUnix = todayUnix / 1000 - 86400;
        String sql = "select ecr.begin_time,ecr.sid,ecr.sname,ecr.tid,ecr.tname,empt.name as book from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where parent <= 0 or parent = id) empt on emst.parent=empt.id\n" +
                "where esi.study_aim=1 and ecr.stype!=''\n" +
                "and ecr.status=3 ";
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String book = cutData[2];
        String classStatus = cutData[3];
        String combo = cutData[4];
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "and ecr.begin_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "and ecr.begin_time between " + bTime + " and " + tTime;
        }
        if (!book.equals("不限")) {
            sql = sql + "\n" + "and empt.name=\"" + book + "\" ";
        }
        if (!classStatus.equals("不限")) {
            if (classStatus.equals("菲律宾")) {
                sql = sql + "\n" + "and ecr.catalog=1";
            } else {
                sql = sql + "\n" + "and ecr.catalog=2";
            }
        }
        switch (combo) {
            case "不限":
                break;
            case "51":
                sql = sql + "\n" + "and ((es.lsns_per_day=1 and es.days_per_week=5) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=5))";
                break;
            case "52":
                sql = sql + "\n" + "and ((es.lsns_per_day=2 and es.days_per_week=5) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=5))";
                break;
            case "31":
                sql = sql + "\n" + "and ((es.lsns_per_day=1 and es.days_per_week=3) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=3))";
                break;
            case "32":
                sql = sql + "\n" + "and ((es.lsns_per_day=2 and es.days_per_week=3) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=3))";
                break;
            case "00":
                sql = sql + "\n" + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                break;
        }
        TableQuery query = new TableQuery(entityManager, CostClass.class, criterias, sql);
        DataSet<CostClass> result = query.getResultDataSet();
        chooseBookCostClassCnt = query.getTotalCount();
        chooseBookWholeSql = sql;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getChooseBookCostClassCnt() {
        return chooseBookCostClassCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getChooseBookWholeSql() {
        return chooseBookWholeSql;
    }

}
