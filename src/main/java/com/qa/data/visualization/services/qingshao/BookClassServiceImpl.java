package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.awt.image.ImageWatched;

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
    List<Object[]> sqlResult = null;
    List<Object[]> sqlCompareResult=null;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;


    @Override
    @RequestMapping
//    @Cacheable(value = "get_book", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String, ArrayList> getBook(String data) throws ParseException {
        LinkedHashMap<String, ArrayList> map = new LinkedHashMap<String, ArrayList>();
        String s=null;
        if(data.contains("---")){
        String[] cutTime = data.split("---");
        SimpleDateFormat dateFormat;
        if(cutTime[0].contains(":")){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutTime[0]);
        long bTime = bDate.getTime()/1000;
        Date tDate=dateFormat.parse(cutTime[1]);
        long tTime=tDate.getTime()/1000;
        s = String.format("select empt.name,empt.status,empt.id,count(ecr.id) as cnt from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=(%s)and ecr.begin_time<=(%s)\n" +
                "and ecr.status=3\n" +
                "and  esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n"+
                "group by empt.name \n" +
                "order by cnt desc", bTime, tTime);
        }else{
            if(data.equals("all")){
                s="select name,status,id from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%'";
            }else{
                if(data.equals("on")){
                    s="select name,status,id from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%' and status=1";
                }else{
                    s="select name,status,id from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%' and status=-1";
                }
            }
        }
        Query q = entityManager.createNativeQuery(s);
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            ArrayList message=new ArrayList();
            message.add(result[0].toString());
            message.add(result[1].toString());
            message.add(result[2].toString());
            map.put(result[0].toString(),message);
        }
        return map;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
//    @Cacheable(value = "get_book_month_choose", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getBookMonthChoose(String data) throws ParseException {
        ArrayList bookName = new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        String[] cutTime = data.split("---");
        SimpleDateFormat dateFormat;
        if(cutTime[0].contains(":")){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutTime[0]);
        long bTime = bDate.getTime()/1000;
        Date tDate=dateFormat.parse(cutTime[1]);
        long tTime=tDate.getTime()/1000;
        LinkedHashMap<String, ArrayList> getBook = getBook(cutTime[2]);
        for (Map.Entry<String, ArrayList> entry : getBook.entrySet()) {
            ArrayList result= entry.getValue();
            bookName.add(result.get(0));
        }
        String s = String.format("select empt.name,count(ecr.id) as count,count(DISTINCT ecr.sid)  from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=(%s) and ecr.begin_time <=(%s)\n" +
                "and ecr.status=3\n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "group by empt.name\n" +
                "order by count desc", bTime, tTime);
        List<Object[]> list = entityManager.createNativeQuery(s).getResultList();
        for (Object aBookName : bookName) {
            boolean find = false;
            for (Object[] result : list) {
                if (result[0].equals(aBookName)) {
                    number0.add(result[1]);
                    number1.add(result[2]);
                    find = true;
                    break;
                }
            }
            if (!find) {
                number0.add(0);
                number1.add(0);
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
    public DataSet<BookMonthChooseTable> getBookMonthChooseTable(DatatablesCriterias criterias, String data) throws ParseException {
        String[] cutTime = data.split("---");
        SimpleDateFormat dateFormat;
        String bookSql;
        if (cutTime[0].contains(":")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        LinkedHashMap<String, ArrayList> book = getBook(cutTime[2]);
        ArrayList bookList=new ArrayList();
        for (Map.Entry<String, ArrayList> entry : book.entrySet()) {
            ArrayList result= entry.getValue();
            bookList.add(result.get(2));
        }
        if(bookList.size()>0){
            bookSql="("+bookList.get(0);
            for(int i=1;i<bookList.size();i++){
                bookSql=bookSql+","+bookList.get(i);
            }
            bookSql=bookSql+")";
        }else{
            bookSql="1=1";
        }
        Date bDate = dateFormat.parse(cutTime[0]);
        long bTime = bDate.getTime() / 1000;
        Date tDate = dateFormat.parse(cutTime[1]);
        long tTime = tDate.getTime() / 1000;
        String s = String.format("select empt.name as book,count(ecr.id) as ccnt,count(DISTINCT ecr.sid) as scnt from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=(%s) and ecr.begin_time <=(%s)\n" +
                "and ecr.status=3\n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "and empt.id in %s" +
                "group by empt.name", bTime, tTime,bookSql);
        TableQuery query = new TableQuery(entityManager, BookMonthChooseTable.class, criterias, s);
        return query.getResultDataSet();
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public DataSet<BookChooseClassStock> getBookChooseClassStockSql(DatatablesCriterias criterias, String data) throws ParseException {
        String [] cutData=data.split("---");
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String limitTime;
        String bookSql;
        if(month==1){
            limitTime=(year-1)+"-11-1";
        }else{
            limitTime=year+"-"+(month-2)+"-1";
        }
        LinkedHashMap<String, ArrayList> book = getBook(cutData[3]);
        ArrayList bookList=new ArrayList();
        for (Map.Entry<String, ArrayList> entry : book.entrySet()) {
            ArrayList result= entry.getValue();
            bookList.add(result.get(2));
        }
        if(bookList.size()>0){
            bookSql="("+bookList.get(0);
            for(int i=1;i<bookList.size();i++){
                bookSql=bookSql+","+bookList.get(i);
            }
            bookSql=bookSql+")";
        }else{
            bookSql="1=1";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime()/1000;
        Date lDate=dateFormat.parse(limitTime);
        long lTime=lDate.getTime()/1000;
        String s;
        if(bTime<lTime){
            s = String.format("select ecr.begin_time*1000 as time,count(ecr.id) as count,empt.id as name,empt.name as book from (select * from ebk_class_records union all select * from ebk_class_records_2016 where begin_time>%s) ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and ecr.status=3\n" +
                    "and empt.id in %s\n" +
                    "group by name,from_unixtime(ecr.begin_time, '%%Y-%%m-%%d')",bTime,bookSql);
        }else{
            s = String.format("select ecr.begin_time*1000 as time,count(ecr.id) as count,empt.id as name,empt.name as book from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and ecr.status=3\n" +
                    "and empt.id in %s\n" +
                    "group by name,from_unixtime(ecr.begin_time, '%%Y-%%m-%%d')",bookSql);
        }
        if (cutData[2].equals("sql")) {
            Query q = entityManager.createNativeQuery(s);
            sqlResult = q.getResultList();
            return null;
        } else {
            TableQuery query = new TableQuery(entityManager, BookChooseClassStock.class, criterias, s);
            return query.getResultDataSet();
        }
    }
    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getBookChooseClassStock(String data) throws ParseException {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (Object[] result : sqlResult) {
            if (result[2].toString().equals(data)) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    @Cacheable(value = "get_student_levels", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getStudentLevels() {
        ArrayList resultList = new ArrayList();
        String s = String.format("select concat(es.level,es.sub_level) as levels from ebk_students es\n" +
                "LEFT JOIN ebk_class_records ecr on es.id=ecr.sid\n" +
                "where es.level is not null and es.sub_level is not null\n" +
                "GROUP BY levels\n");
        Query q = entityManager.createNativeQuery(s);
        List list = q.getResultList();
        for (Object aList : list) {
            resultList.add(aList.toString());
        }
        return resultList;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    @Cacheable(value = "get_book_rank_choose_class", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getBookRankChooseClass(String data) throws ParseException {
        ArrayList levels = new ArrayList();
        ArrayList relLevels=new ArrayList();
        ArrayList number0 = new ArrayList();
        ArrayList number1 = new ArrayList();
        ArrayList message = new ArrayList();
        String[] cutData = data.split("---");
        SimpleDateFormat dateFormat;
        if(cutData[0].contains(":")){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime()/1000;
        Date tDate=dateFormat.parse(cutData[1]);
        long tTime=tDate.getTime()/1000;
        if (cutData[3].equals("ALL")) {
            levels = getStudentLevels();
        } else {
            levels.add(cutData[3]);
        }
        String s = String.format("select concat(es.level,es.sub_level) AS level,count(ecr.id),count(DISTINCT ecr.sid) from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                "and ecr.status=3\n" +
                "and es.sub_level is not null\n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "and empt.id=\"%s\"\n" +
                "group by concat(es.level,es.sub_level)", bTime, tTime, cutData[2]);
        Query q = entityManager.createNativeQuery(s);
        List<Object[]> list=q.getResultList();
        for (Object level : levels) {
            boolean find=false;
            for (Object[] result : list) {
                if (result[0].equals(level)) {
                    relLevels.add(result[0]);
                    number0.add(result[1]);
                    number1.add(result[2]);
                    find = true;
                    break;
                }
            }
            if (!find) {
                relLevels.add(level);
                number0.add(0);
                number1.add(0);
            }
        }
        message.add(relLevels);
        message.add(number0);
        message.add(number1);
        return message;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public DataSet<BookRankChooseClassTable> getBookRankChooseClassTable(DatatablesCriterias criterias, String data) throws ParseException {
        String[] cutData = data.split("---");
        SimpleDateFormat dateFormat;
        if (cutData[0].contains(":")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime() / 1000;
        Date tDate = dateFormat.parse(cutData[1]);
        long tTime = tDate.getTime() / 1000;
        String s = String.format("select concat(es.level,es.sub_level) as level,count(ecr.id) as ccnt,count(DISTINCT ecr.sid) as scnt from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                "and ecr.status=3\n" +
                "and es.sub_level is not null\n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "and empt.id=\"%s\"\n" +
                "group by concat(es.level,es.sub_level)", bTime, tTime, cutData[2]);
        if (!cutData[3].equals("ALL")) {
            s = String.format("select concat(es.level,es.sub_level) as level,count(ecr.id) as ccnt,count(DISTINCT ecr.sid) as scnt from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                    "and ecr.status=3\n" +
                    "and es.sub_level is not null\n" +
                    "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and empt.id=\"%s\"\n" +
                    "and concat(es.level,es.sub_level)='%s'", bTime, tTime, cutData[2], cutData[3]);
        }
        TableQuery query = new TableQuery(entityManager, BookRankChooseClassTable.class, criterias, s);
        return query.getResultDataSet();
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public DataSet<BookRankAgeTable> getBookRankAgeTable(DatatablesCriterias criterias, String data) throws ParseException {
        String[] cutData = data.split("---");
        SimpleDateFormat dateFormat;
        if (cutData[0].contains(":")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime() / 1000;
        Date tDate = dateFormat.parse(cutData[1]);
        long tTime = tDate.getTime() / 1000;
        String s = String.format("select count(DISTINCT ecr.sid) as cnt,esi.age_duration as age,concat(es.level,es.sub_level) as level from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                "and ecr.status=3\n" +
                "and es.sub_level is not null \n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "and empt.id=\"%s\"\n" +
                "and esi.age_duration in('1','2','3','10','11','12','6','7','8','9') \n" +
                "GROUP BY esi.age_duration,concat(es.level,es.sub_level)", bTime, tTime, cutData[2]);
        if (!cutData[3].equals("ALL")) {
            s = String.format("select count(DISTINCT ecr.sid) as cnt,esi.age_duration as age,concat(es.level,es.sub_level) as level from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                    "and ecr.status=3\n" +
                    "and es.sub_level is not null \n" +
                    "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and empt.id=\"%s\"\n" +
                    "and concat(es.level,es.sub_level)='%s'\n" +
                    "and esi.age_duration in('1','2','3','10','11','12','6','7','8','9') \n" +
                    "GROUP BY esi.age_duration,concat(es.level,es.sub_level)", bTime, tTime, cutData[2], cutData[3]);
        }
        TableQuery query = new TableQuery(entityManager, BookRankAgeTable.class, criterias, s);
        return query.getResultDataSet();
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    @Cacheable(value = "get_book_rank_age", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getBookRankAge(String data) throws ParseException {
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
        SimpleDateFormat dateFormat;
        if(cutData[0].contains(":")){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime()/1000;
        Date tDate=dateFormat.parse(cutData[1]);
        long tTime=tDate.getTime()/1000;
        if (cutData[3].equals("ALL")) {
            levels = getStudentLevels();
        } else {
            levels.add(cutData[3]);
        }
        String [] ageDuration={"1","2","3","10","11","12","6","7","8","9"};
        String s = String.format("select count(DISTINCT ecr.sid),esi.age_duration,concat(es.level,es.sub_level) from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where ecr.begin_time >=%s and ecr.begin_time <=%s\n" +
                "and ecr.status=3\n" +
                "and es.sub_level is not null \n" +
                "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                "and empt.id=\"%s\"\n" +
                "GROUP BY esi.age_duration,concat(es.level,es.sub_level) \n" +
                "order by concat(es.level,es.sub_level)", bTime, tTime, cutData[2]);
        Query q = entityManager.createNativeQuery(s);
            List<Object[]> list = q.getResultList();
            for (Object level : levels) {
                for (String anAgeDuration : ageDuration) {
                    boolean find = false;
                    for (Object[] result : list) {
                        if (result[1].toString().equals(anAgeDuration) && result[2].equals(level)) {
                            switch (anAgeDuration) {
                                case "1":
                                    number0.add(result[0]);
                                    break;
                                case "2":
                                    number1.add(result[0]);
                                    break;
                                case "3":
                                    number2.add(result[0]);
                                    break;
                                case "10":
                                    number3.add(result[0]);
                                    break;
                                case "11":
                                    number4.add(result[0]);
                                    break;
                                case "12":
                                    number5.add(result[0]);
                                    break;
                                case "6":
                                    number6.add(result[0]);
                                    break;
                                case "7":
                                    number7.add(result[0]);
                                    break;
                                case "8":
                                    number8.add(result[0]);
                                    break;
                                case "9":
                                    number9.add(result[0]);
                                    break;
                            }
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        switch (anAgeDuration) {
                            case "1":
                                number0.add(0);
                                break;
                            case "2":
                                number1.add(0);
                                break;
                            case "3":
                                number2.add(0);
                                break;
                            case "10":
                                number3.add(0);
                                break;
                            case "11":
                                number4.add(0);
                                break;
                            case "12":
                                number5.add(0);
                                break;
                            case "6":
                                number6.add(0);
                                break;
                            case "7":
                                number7.add(0);
                                break;
                            case "8":
                                number8.add(0);
                                break;
                            case "9":
                                number9.add(0);
                                break;
                        }
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
    public DataSet<BookChooseClassStock> getBookRankChooseCompareSql(DatatablesCriterias criterias, String data) throws ParseException {
        String[] cutData = data.split("---");
        String level=cutData[2];
        String type=cutData[3];
        String bookStatus=cutData[4];
        String sql;
        String bookSql;
        if (level.equals("ALL")) {
            sql = "concat(es.level,es.sub_level) in( '0', '1', '2', '3A', '3B', '4A', '4B', '4C', '5A', '5B', '5C', '6A', '6B', '6C', '7A', '7B', '7C', '8A', '8B')";
        } else {
            sql = "concat(es.level,es.sub_level)='" + level + "'";
        }
        LinkedHashMap<String, ArrayList> book = getBook(bookStatus);
        ArrayList bookList=new ArrayList();
        for (Map.Entry<String, ArrayList> entry : book.entrySet()) {
            ArrayList result= entry.getValue();
            bookList.add(result.get(2));
        }
        if(bookList.size()>0){
            bookSql="("+bookList.get(0);
            for(int i=1;i<bookList.size();i++){
                bookSql=bookSql+","+bookList.get(i);
            }
            bookSql=bookSql+")";
        }else{
            bookSql="1=1";
        }
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String limitTime;
        if(month==1){
            limitTime=(year-1)+"-11-1 00:00";
        }else{
            limitTime=year+"-"+(month-2)+"-1 00:00";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date bDate = dateFormat.parse(cutData[0]);
        long bTime = bDate.getTime()/1000;
        Date lDate=dateFormat.parse(limitTime);
        long lTime=lDate.getTime()/1000;
        String s;
        if(lTime>bTime){
            s = String.format("select ecr.begin_time*1000 as time,count(ecr.id) as count,empt.id as name,cmpt.name as book from (select * from ebk_class_records union all select * from ebk_class_records_2016 where begin_time>%s) ecr " +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and ecr.status=3\n" +
                    "and %s \n" +
                    "and empt.id in %s \n" +
                    "group by from_unixtime(ecr.begin_time, '%%Y-%%m-%%d'),empt.name",bTime, sql,bookSql);
            Query q = entityManager.createNativeQuery(s);
            sqlCompareResult = q.getResultList();
        }else{
            s = String.format("select ecr.begin_time*1000 as time,count(ecr.id) as count,empt.id as name,empt.name as book from ebk_class_records ecr\n" +
                    "LEFT JOIN ebk_students es on ecr.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                    "LEFT JOIN ebk_materials_small_type emst on emst.id=ecr.stype\n" +
                    "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                    "where esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +
                    "and ecr.status=3\n" +
                    "and %s\n" +
                    "and empt.id in %s \n" +
                    "group by from_unixtime(ecr.begin_time, '%%Y-%%m-%%d'),empt.name", sql,bookSql);
            Query q = entityManager.createNativeQuery(s);
            sqlCompareResult = q.getResultList();
        }
        if (type.equals("sql")) {
            Query q = entityManager.createNativeQuery(s);
            sqlResult = q.getResultList();
            return null;
        } else {
            TableQuery query = new TableQuery(entityManager, BookChooseClassStock.class, criterias, s);
            return query.getResultDataSet();
        }
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getBookRankChooseClassCompare(String data) throws ParseException {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (Object[] result : sqlCompareResult) {
            if (result[2].toString().equals(data)) {
                map.put(result[0].toString(), result[1].toString());
            }
        }
        return map;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public DataSet<ManagerCostClass> getBookChooseCostClass(String data, DatatablesCriterias criterias) throws ParseException {
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
                "LEFT JOIN (select * from ebk_materials_small_type where (parent <= 0 or parent = id) and for_course like '1%%') empt on emst.parent=empt.id\n" +
                "where" ;
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String book = cutData[2];
        String classStatus = cutData[3];
        String combo = cutData[4];
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "ecr.begin_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "ecr.begin_time>=" + bTime + " and ecr.begin_time<=" + tTime;
        }
        sql=sql+"\n"+ "and esi.study_aim=1 and ecr.stype!='' and empt.name is not null\n" +"and ecr.status=3 ";
        if (!book.equals("不限")) {
            sql = sql + "\n" + "and empt.id='" + book + "' ";
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
        TableQuery query = new TableQuery(entityManager, ManagerCostClass.class, criterias, sql);
        DataSet<ManagerCostClass> result = query.getResultDataSet();
        chooseBookCostClassCnt = query.getTotalCount();
        chooseBookWholeSql = sql;
        return result;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public Long getChooseBookCostClassCnt() {
        return chooseBookCostClassCnt;
    }

    @Override
    @RequestMapping
    @SuppressWarnings("unchecked")
    public String getChooseBookWholeSql() {
        return chooseBookWholeSql;
    }

}
