package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.repositories.qingshao.EbkStudentsRepository;
import com.qa.data.visualization.repositories.qingshao.EbkTeachersRepository;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dykj on 2016/11/17.
 */
@Service
public class ClassServiceImpl implements ClassService {
    private Long costClassCnt;
    private String wholeSql;
    private Long costSaClassCnt;
    private String wholeSaSql;
    private Long newStudentCnt;
    private String newStudentSql;
    private String newStudentPayCnt;
    private Long oldStudentCnt;
    private String oldStudentSql;
    private String oldStudentPayCnt;
    private String workStudentMessageSql;
    private Long workStudentMessageCnt;
    private Long workClassMessageCnt;
    private String workClassMessageSql;
    private String workRenewSql;
    private String workRenewWholeSql;
    private String workRefundsSql;
    private ArrayList workRefundsCnt=new ArrayList();
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager firstEntityManager;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;
    @Autowired
    private EbkStudentsRepository ebkStudentsRepository;

    @Autowired
    private EbkTeachersRepository ebkTeachersRepository;

    @Override
    public List<AutoComplete> getStudentAutoComplete(String query) {
        List<AutoComplete> autoCompleteList = new ArrayList<>();
        Pageable top10 = new PageRequest(0, 10);
        List<EbkStudent> ebkStudentsList;
        if (query.startsWith("13")) {
            ebkStudentsList = ebkStudentsRepository.getEbkStudentsByMobile(query, top10);
        } else {
            ebkStudentsList = ebkStudentsRepository.getEbkStudentsById(query, top10);
        }
        for (EbkStudent ebkStudent : ebkStudentsList) {
            AutoComplete autoComplete = new AutoComplete();
            autoComplete.setValue(String.valueOf(ebkStudent.getId()));
            autoComplete.setData(ebkStudent.getNickname());
            autoCompleteList.add(autoComplete);
        }
        return autoCompleteList;
    }

    @Override
    public List<AutoComplete> getTeacherAutoComplete(String query) {
        List<AutoComplete> autoCompleteList = new ArrayList<>();
        Pageable top10 = new PageRequest(0, 10);
        List<EbkTeacher> ebkTeachersList;
        if (query.startsWith("13")) {
            ebkTeachersList = ebkTeachersRepository.getEbkTeachersByEmail(query, top10);
        } else {
            ebkTeachersList = ebkTeachersRepository.getEbkTeachersById(query, top10);
        }
        for (EbkTeacher ebkTeacher : ebkTeachersList) {
            AutoComplete autoComplete = new AutoComplete();
            autoComplete.setValue(String.valueOf(ebkTeacher.getId()));
            autoComplete.setData(ebkTeacher.getNickname());
            autoCompleteList.add(autoComplete);
        }
        return autoCompleteList;
    }


    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getTeacherGroup() {
        ArrayList getGroup = new ArrayList();
        String s = String.format("select distinct ebk_teacher_group.title\n" +
                "from ebk_teachers inner join ebk_teacher_group on ebk_teachers.workgroup = ebk_teacher_group.id\n" +
                "where ebk_teachers.tch_course = 1 and ebk_teachers.is_tester=0 order by title ");
        Query q = entityManager.createNativeQuery(s);
        List list = q.getResultList();
        for (Object aList : list) {
            getGroup.add(aList.toString());
        }
        return getGroup;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getSaTeacherGroup() {
        ArrayList getGroup = new ArrayList();
        String s = String.format("select distinct ebk_teacher_group.title\n" +
                "from ebk_teachers inner join ebk_teacher_group on ebk_teachers.workgroup = ebk_teacher_group.id\n" +
                "where ebk_teachers.tch_course = 1 and ebk_teachers.is_tester=1 order by title");
        Query q = entityManager.createNativeQuery(s);
        List list = q.getResultList();
        for (Object aList : list) {
            getGroup.add(aList.toString());
        }
        return getGroup;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<CostClass> getCostClass(String data, DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime();
        long yesterdayUnix = todayUnix / 1000 - 86400;
        String aSql="select ecr.begin_time as begin_time,ecr.tid as tid,ecr.tname as tname,ecr.sid as sid,ecr.sname as sname  \n" +
                "from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid = es.id\n" +
                "LEFT JOIN ebk_student_info esi on ecr.sid = esi.sid\n" +
                "LEFT JOIN ebk_teachers et on ecr.tid = et.id\n" +
                "LEFT JOIN ebk_teacher_group etg on et.workgroup = etg.id" ;
        String bSql="";
        String sql;
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String studentMessage = cutData[2];
        String comboCountry = cutData[3];
        String combo = cutData[4];
        String binding = cutData[5];
        String teacherMessage = cutData[6];
        String teacherStatus = cutData[7];
        String group = cutData[8];
        String teacherType = cutData[9];
        String classStatus=cutData[10];
        String classType=cutData[11];
        if (bTime.equals("all") || tTime.equals("all")) {
            bSql = bSql + "\n" + "ecr.begin_time>" + yesterdayUnix;
        } else {
            bSql = bSql + "\n" + "ecr.begin_time>=" + bTime + " and ecr.begin_time<=" + tTime;
        }
        bSql=bSql+"\n"+"and ecr.status=3\n" +"and esi.study_aim=1";
        if (!studentMessage.equals("all")) {
            bSql = bSql + "\n" + "and es.id= " + studentMessage;
        }
        if (comboCountry.equals("不限")) {
            switch (combo) {
                case "不限":
                    break;
                case "51":
                    bSql = bSql + "\n" + "and ((es.lsns_per_day=1 and es.days_per_week=5) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=5))";
                    break;
                case "52":
                    bSql = bSql + "\n" + "and ((es.lsns_per_day=2 and es.days_per_week=5) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=5))";
                    break;
                case "31":
                    bSql = bSql + "\n" + "and ((es.lsns_per_day=1 and es.days_per_week=3) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=3))";
                    break;
                case "32":
                    bSql = bSql + "\n" + "and ((es.lsns_per_day=2 and es.days_per_week=3) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=3))";
                    break;
                case "00":
                    bSql = bSql + "\n" + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                    break;
            }
        } else {
            if (comboCountry.equals("菲律宾")) {
                switch (combo) {
                    case "不限":
                        bSql = bSql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                        break;
                    case "51":
                        bSql = bSql + "\n" + "and (es.lsns_per_day=1 and es.days_per_week=5)";
                        break;
                    case "52":
                        bSql = bSql + "\n" + "and (es.lsns_per_day=2 and es.days_per_week=5)";
                        break;
                    case "00":
                        aSql=aSql+ "\n LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                                "group by eaod.order_id,eaod.begin_time\n" +
                                "union\n" +
                                "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                                "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id";
                        bSql = bSql + "\n" + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                        bSql = bSql+"\n"+"and (allorder.tch_from=1)";
                        break;
                }
            } else {
                switch (combo) {
                    case "不限":
                        bSql = bSql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                        break;
                    case "51":
                        bSql = bSql + "\n" + "and (es.lsns_per_day_eu=1 and es.days_per_week_eu=5)";
                        break;
                    case "52":
                        bSql = bSql + "\n" + "and (es.lsns_per_day_eu=2 and es.days_per_week_eu=5)";
                        break;
                    case "31":
                        bSql = bSql + "\n" + "and (es.lsns_per_day_eu=1 and es.days_per_week_eu=3)";
                        break;
                    case "32":
                        bSql = bSql + "\n" + "and (es.lsns_per_day_eu=2 and es.days_per_week_eu=3)";
                        break;
                    case "00":
                        aSql=aSql+ "\n LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                                "group by eaod.order_id,eaod.begin_time\n" +
                                "union\n" +
                                "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                                "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id";
                        bSql = bSql + "\n" + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                        bSql = bSql+"\n"+"and (allorder.tch_from=2)";
                        break;
                }
            }
        }
        if (!binding.equals("不限")) {
            switch (binding) {
                case "绑定":
                    bSql = bSql + "\n" + "and ecr.is_bind=1";
                    break;
                case "不绑定":
                    bSql = bSql + "\n" + "and ecr.is_bind=-1";
                    break;
            }
        }
        if (!teacherMessage.equals("all")) {
            bSql = bSql + "\n" + "and et.id=" + teacherMessage;
        }
        if (!group.equals("Group")) {
            bSql = bSql + "\n" + "and etg.title='" + group + "'";
        }
        if (!teacherStatus.equals("不限")) {
            switch (teacherStatus) {
                case "试用":
                    bSql = bSql + "\n" + "and et.status=3";
                    break;
                case "活跃":
                    bSql = bSql + "\n" + "and et.status=4";
                    break;
            }
        }
        if (!teacherType.equals("不限")) {
            switch (teacherType) {
                case "菲律宾":
                    bSql = bSql + "\n" + "and et.catalog=1";
                    break;
                case "欧美":
                    bSql = bSql + "\n" + "and et.catalog=2";
                    break;
            }
        }
        if(!classStatus.equals("不限")){
            if(classStatus.equals("完成")){
                bSql=bSql+"\n"+"and ecr.status=3";
            }else{
                bSql=bSql+"\n"+"and ecr.status=5";
            }
        }else{
            bSql=bSql+"\n"+"and ecr.status!=-1";
        }
        if(!classType.equals("不限")){
            switch(classType){
                case "常规课程":bSql=bSql+"\n"+"and ecr.free_try=0";break;
                case "免费体验":bSql=bSql+"\n"+"and ecr.free_try=1";break;
                case "免费测评":bSql=bSql+"\n"+"and ecr.free_try=2";break;
                case "常规测评":bSql=bSql+"\n"+"and ecr.free_try=3";break;
            }
        }
        bSql=bSql+"\n group by ecr.sid,begin_time";
        sql=aSql+"\n where"+bSql;
        TableQuery query = new TableQuery(entityManager, CostClass.class, criterias, sql);
        DataSet<CostClass> result = query.getResultDataSet();
        costClassCnt = query.getTotalCount();
        wholeSql = sql;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<CostSaClass> getCostSaClass(String data, DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime();
        long yesterdayUnix = todayUnix / 1000 - 86400;
        String sql = "select ecr.begin_time as begin_time,ecr.tid as tid,ecr.tname as tname,ecr.sid as sid,ecr.sname as sname,ecr.status as status  \n" +
                "from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid = es.id\n" +
                "LEFT JOIN ebk_student_info esi on ecr.sid = esi.sid\n" +
                "LEFT JOIN ebk_teachers et on ecr.tid = et.id\n" +
                "LEFT JOIN ebk_teacher_group etg on et.workgroup = etg.id\n" +
                "where ";
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String studentMessage = cutData[2];
        String teacherMessage = cutData[3];
        String teacherStatus = cutData[4];
        String group = cutData[5];
        String teacherType = cutData[6];
        String classType = cutData[7];
        String classStatus = cutData[8];
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "ecr.begin_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "ecr.begin_time>=" + bTime + " and ecr.begin_time<=" + tTime;
        }
        sql=sql+"\n"+"and esi.study_aim=1";
        if (!studentMessage.equals("all")) {
            sql = sql + "\n" + "and es.id= " + studentMessage;
        }
        if (!teacherMessage.equals("all")) {
            sql = sql + "\n" + "and et.id=" + teacherMessage;
        }
        if (!group.equals("Group")) {
            sql = sql + "\n" + "and etg.title='" + group + "'";
        }
        if (teacherStatus.equals("不限")) {
            sql = sql + "\n" + "and (et.status=3 or et.status=4)";
        } else {
            switch (teacherStatus) {
                case "试用":
                    sql = sql + "\n" + "and et.status=3";
                    break;
                case "活跃":
                    sql = sql + "\n" + "and et.status=4";
                    break;
            }
        }
        if (!teacherType.equals("不限")) {
            switch (teacherType) {
                case "菲律宾":
                    sql = sql + "\n" + "and et.catalog=1";
                    break;
                case "欧美":
                    sql = sql + "\n" + "and et.catalog=2";
                    break;
            }
        }
        if (classType.equals("不限")) {
            sql = sql + "\n" + "and (ecr.free_try=2 or ecr.free_try=3)";
        } else {
            switch (classType) {
                case "常规测评课":
                    sql = sql + "\n" + "and ecr.free_try=3";
                    break;
                case "免费测评课":
                    sql = sql + "\n" + "and ecr.free_try=2";
                    break;
            }
        }
        if (classStatus.equals("不限")) {
            sql = sql + "\n" + "and (ecr.status=3 or ecr.status=4 or ecr.status=6 or ecr.status=8 or ecr.status=9)";
        } else {
            switch (classStatus) {
                case "res":
                    sql = sql + "\n" + "and (ecr.status=4 or ecr.status=6 or ecr.status=8)";
                    break;
                case "顺利结束":
                    sql = sql + "\n" + "and (ecr.status=3 or ecr.status=9)";
                    break;
            }
        }
        sql=sql+"\n"+"group by ecr.sid,ecr.begin_time";
        TableQuery query = new TableQuery(entityManager, CostSaClass.class, criterias, sql);
        DataSet<CostSaClass> result = query.getResultDataSet();
        costSaClassCnt = query.getTotalCount();
        wholeSaSql = sql;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<payStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime();
        long yesterdayUnix = todayUnix / 1000 - 86400;
        String sql = String.format("select es.create_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ifnull(eao.tmoney,0) as cnt from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                "LEFT JOIN ebk_advertisement_source eas on eas.id=esi.knowus\n" +
                "LEFT JOIN ebk_acoin_orders eao on es.id = eao.sid\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                "where ");
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String regWay = cutData[2];
        String changeData = cutData[3];
        String tableShow = cutData[4];
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "es.create_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "es.create_time >=" + bTime + " and es.create_time<=" + tTime;
        }
        sql=sql+"\n"+"and esi.study_aim=1";
        if (!regWay.equals("不限")) {
            if (regWay.equals("转介绍")) {
                sql = sql + "\n" + "and esi.referral=1";
                if (!changeData.equals("不限")) {
                    if (changeData.equals("渠道代理")) {
                        sql = sql + "\n" + "and es.id in(select sid from ebk_agent_code_student)";
                    } else {
                        sql = sql + "\n" + "and es.id not in(select sid from ebk_agent_code_student)";
                    }
                }
            } else {
                int levelNumber=0;
                sql = sql + "\n" + "and esi.referral=-1";
                if (!changeData.equals("不限")) {
                    switch(changeData){
                        case "S":levelNumber=1;break;
                        case "A":levelNumber=2;break;
                        case "B":levelNumber=3;break;
                        case "C":levelNumber=4;break;
                    }
                    sql = sql + "\n" + "and eas.level=" + levelNumber + "";
                }
            }
        }
        switch (tableShow) {
            case "注册量":
                break;
            case "预约量":
                sql = sql + "\n" + "and (esi.mfcp_crm=2 or esi.mfcp_crm=3 or esi.mfcp_crm=5 or esi.mfcp_crm=6) ";
                break;
            case "完成测评课数量":
                sql = sql + "\n" + "and esi.mfcp_crm=3";
                break;
            case "销售量及购买人数":
                sql = sql + "\n" + "and eao.payed=1";
                break;
        }
        sql = sql + "\n" + "group by es.id,es.create_time";
        TableQuery query = new TableQuery(entityManager, payStudent.class, criterias, sql);
        DataSet<payStudent> result = query.getResultDataSet();
        newStudentCnt = query.getTotalCount();
        newStudentSql = sql;
        if (tableShow.equals("销售量及购买人数")) {
            String s = String.format("select sum(result.cnt) from (%s) result", sql);
            Query q = entityManager.createNativeQuery(s);
            List list = q.getResultList();
            if (list.get(0) == null) {
                newStudentPayCnt = "0";
            } else {
                newStudentPayCnt = list.get(0).toString();
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<payStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime();
        long yesterdayUnix = todayUnix / 1000 - 86400;
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String teacherType = cutData[2];
        String classType = cutData[3];
        String studentStatus = cutData[4];
        String classShowType = cutData[5];
        String sql=null;
        if(studentStatus.equals("退款学员")){
            sql=String.format("select er.refund_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ROUND(ifnull(er.money,0),2) as cnt \n" +
                    "from ebk_refunds er \n" +
                    "LEFT JOIN ebk_students es on er.sid=es.id\n" +
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                    "LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "where ");
            if (bTime.equals("all") || tTime.equals("all")) {
                sql = sql + "\n" + "er.refund_time>" + yesterdayUnix;
            } else {
                sql = sql + "\n" + "er.refund_time >=" + bTime + " and er.refund_time<=" + tTime;
            }
            sql=sql+"\n"+"and esi.study_aim=1 \n"+"and er.status=3";
        }else{
            sql = String.format("select allorder.create_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ROUND(ifnull(allorder.tmoney,0),2) as cnt \n" +
                    "from ebk_students es\n" +
                    "LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                    "where ");
            if (bTime.equals("all") || tTime.equals("all")) {
                sql = sql + "\n" + "allorder.create_time>" + yesterdayUnix;
            } else {
                sql = sql + "\n" + "allorder.create_time >=" + bTime + " and allorder.create_time<=" + tTime;
            }
            sql=sql+"\n"+"and esi.study_aim=1 \n"+"and allorder.payed=1";
        }
        if (!teacherType.equals("不限")) {
            if (teacherType.equals("菲律宾")) {
                sql = sql + "\n" + "and allorder.tch_from=1";
            } else {
                sql = sql + "\n" + "and allorder.tch_from=2";
            }
        }
        if (!classType.equals("不限")) {
            if (classType.equals("套餐课")) {
                sql = sql + "\n" + "and (allorder.combo_name not like '%自由%' or allorder.combo_name not like '%自由%')" ;
            } else {
                sql = sql + "\n" + "and  (allorder.combo_name like '%自由%' or allorder.combo_name like '%自由%')";
            }
        }
        if (studentStatus.equals("上课中学员")) {
            sql = sql + "\n" + "and es.status=1";
        }
        if (!classShowType.equals("总人数")) {
            if (classShowType.equals("补升人数和金额")) {
                sql = sql + "\n" + "and allorder.order_flag=2 and allorder.upgrade_from!=0";
            } else {
                if(classShowType.equals("新买")){
                    sql=sql+"\n"+"and allorder.order_flag=1";
                }else{
                    sql = sql + "\n" + "and allorder.order_flag=2 and allorder.upgrade_from=0";
                }
            }
        }
        if(studentStatus.equals("退款学员")){
            sql=sql+"\n"+"group by es.id,er.refund_time";
        }else{
            sql = sql + "\n" + "group by es.id,allorder.id,cnt";
        }
        TableQuery query = new TableQuery(entityManager, payStudent.class, criterias, sql);
        DataSet<payStudent> result = query.getResultDataSet();
        oldStudentCnt = query.getTotalCount();
        oldStudentSql = sql;
        String s = String.format("select sum(result.cnt) from (%s) result order by result.sid ", sql);
        Query q = entityManager.createNativeQuery(s);
        List list = q.getResultList();
        if (list.get(0) == null) {
            oldStudentPayCnt = "0";
        } else {
            oldStudentPayCnt = list.get(0).toString();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkStudentMessage> getWorkStudentMessage(String data, DatatablesCriterias criterias) throws ParseException{
        String []cutData=data.split("\\+");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String bTime =cutData[0];
        String tTime=cutData[1];
        Date now = new Date();
        String today = dateFormat.format(now);
        Date date = dateFormat.parse(today);
        long todayUnix = date.getTime()/1000;
        String ageSql="";
        String studyTime="";
        if(!cutData[2].equals("allAge")){
            if(cutData[2].equals("0")){
                ageSql="\nand esi.age_duration not in(1,2,3,10,11,12,6,7,8,9)";
            }else{
                ageSql="\nand esi.age_duration="+cutData[2];
            }
        }
        if(!cutData[3].equals("allStudyTime")){
            switch (cutData[3]){
                case "less1": studyTime="\nand allorder.begin_time<="+(todayUnix)+" and allorder.begin_time>="+(todayUnix-86400*30);break;
                case "1to3":studyTime="\nand allorder.begin_time<"+(todayUnix-86400)+" and allorder.begin_time>="+(todayUnix-86400*90);break;
                case "3to6":studyTime="\nand allorder.begin_time<"+(todayUnix-86400*90)+" and allorder.begin_time>="+(todayUnix-86400*180);break;
                case "6to12":studyTime="\nand allorder.begin_time<"+(todayUnix-86400*180)+" and allorder.begin_time>="+(todayUnix-86400*365);break;
                case "1to2":studyTime="\nand allorder.begin_time<"+(todayUnix-86400*365)+" and allorder.begin_time>="+(todayUnix-86400*730);break;
                case "more2":studyTime="\nand allorder.begin_time<"+(todayUnix-86400*730);break;
            }
        }

        String sql=String.format("select es.id as id,es.nickname as name,esi.age_duration as age,IFNULL(concat(es.level,es.sub_level),'无等级') as level,esi.study_aim as aim,round((("+todayUnix+"-Min(allorder.begin_time))/86400),0) as time from ebk_students es \n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN(select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eaod.begin_time,eao.pay_time from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                "group by eaod.order_id,eaod.begin_time\n" +
                "union\n" +
                "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,easo.begin_time,eao.pay_time from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n" +
                "where ");
        sql = sql + "\n" + "allorder.pay_time >=" + bTime + " and allorder.pay_time<=" + tTime;
        sql=sql+"\n"+"and esi.study_aim=1 and allorder.begin_time is not null and allorder.begin_time!=0";
        sql=sql+ageSql;
        sql=sql+studyTime;
        sql=sql+"\ngroup by es.id";
        TableQuery query = new TableQuery(entityManager, WorkStudentMessage.class, criterias, sql);
        DataSet<WorkStudentMessage> actions=query.getResultDataSet();
        workStudentMessageCnt=query.getTotalCount();
        workStudentMessageSql=sql;
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkStudentRecommend> getWorkStudentRecommend(String data, DatatablesCriterias criterias){
        String []cutData=data.split("\\+");
        String sql=String.format("select es.id,es.nickname as name,rcmd.cnt as count from ebk_students es \n" +
                "INNER JOIN (select rcmd_id ,count(rcmd_id) as cnt from ebk_students where create_time>=%s and create_time<=%s group by rcmd_id) rcmd on rcmd.rcmd_id=es.mobile \n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id \n"+
                "where es.mobile !='' and esi.study_aim=1",cutData[0],cutData[1]);
        TableQuery query = new TableQuery(entityManager, WorkStudentRecommend.class, criterias, sql);
        DataSet<WorkStudentRecommend> actions=query.getResultDataSet();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkLoseStudentClass> getWorkLoseStudentClass(String data,DatatablesCriterias criterias){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String combo=cutData[2];
        String sql=String.format("select es.id as id,es.nickname as name,eru.nickname as ghs,FROM_UNIXTIME(ecr.begin_time,'%%Y-%%m-%%d') as day from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_class_records ecr on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                "where ecr.begin_time>=%s and ecr.begin_time<=%s\n" +
                "and esi.study_aim=1 and es.status=1" ,bTime,tTime);
        if(!combo.equals("不限")){
            switch (combo) {
                case "菲律宾套餐":
                    sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                    break;
                case "欧美套餐":
                    sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                    break;
                case "自由课":
                    sql = sql + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                    break;
            }
        }
        sql=sql+"\n"+"group by es.id,day";
        Query q = entityManager.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        HashMap<String, Object[]> map = new HashMap<String, Object[]>();
        for(int i=0;i<list.size();i++){
            map.put(i+"",list.get(i));
        }
        Long bbTime=Long.parseLong(bTime)*1000;
        Long ttTime=Long.parseLong(tTime)*1000;
        Long oneDay = 1000 * 60 * 60 * 24L;
        ArrayList days=new ArrayList();
        ArrayList ids=new ArrayList();
        ArrayList ghs=new ArrayList();
        ArrayList names=new ArrayList();
        Boolean find=false;
        Boolean haveLose=false;
        int resultCnt=0;
        ArrayList cntList=new ArrayList();
        while (bbTime <= ttTime) {
            Date d = new Date(bbTime);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            days.add(df.format(d));
            bbTime += oneDay;
        }
        System.out.println(days);
        for (int j=0;j<map.size();j++) {
            find = false;
            Object[] result =map.get(j+"");
            for (Object id : ids) {
                if (result[0] .equals(id) ) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                ids.add(result[0]);
                names.add(result[1]);
                ghs.add(result[2]);
            }
        }
        System.out.println(ids);
        System.out.println(names);
        System.out.println(ghs);
        for (Object id : ids) {
            haveLose=false;
            int dayRun=0;
            for (int k=0;k<map.size();k++) {
                Object[] result =map.get(k+"");
                if (result[0].equals(id)) {
                    int cnt=0;
                    if(k!=0){
                        Object[] lastResult=map.get((k-1)+"");
                        if(!lastResult[0].equals(result[0])){
                            dayRun=0;
                        }
                    }else {
                        dayRun=0;
                    }
                    for (int i=dayRun;i<days.size();i++) {
                        if (result[3].equals(days.get(i))) {
                            dayRun=i+1;
                            break;
                        } else {
                            cnt = cnt + 1;
                        }
                    }
                    if(cnt>resultCnt){
                        resultCnt=cnt;
                        haveLose=true;
                    }
                }
            }
            if(!haveLose){
                resultCnt=0;
            }
            cntList.add(resultCnt);
            resultCnt=0;
        }
        System.out.print(cntList);
        TableQuery query = new TableQuery(entityManager, WorkLoseStudentClass.class, criterias, sql);
        DataSet<WorkLoseStudentClass> actions=query.getResultDataSet();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkLoseStudentAcoin> getWorkLoseStudentAcoin(String data,DatatablesCriterias criterias){
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet getWorkRenew(String data,DatatablesCriterias criterias){
        String[] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String teacherType = cutData[2];
        String classType = cutData[3];
        String renewType=cutData[4];
        String look=cutData[5];
        String sql;
        if(look.equals("table1")){
        sql = String.format("select allorder.id as oid,es.id as id,es.nickname as name,allorder.up as type, ROUND(ifnull(allorder.tmoney,0),2) as money \n" +
                "from ebk_students es\n" +
                "LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                "group by eaod.order_id,eaod.begin_time\n" +
                "union\n" +
                "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                "where allorder.create_time >=%s and allorder.create_time<=%s\n" +
                "and esi.study_aim=1 and allorder.payed=1 and allorder.order_flag=2",bTime,tTime);

        workRenewWholeSql=sql;
        }else{
            sql = String.format("select es.id as id,es.nickname as name, ifNUll(eru.nickname,'UNKNOWN') as ghs\n" +
                    "from ebk_students es\n" +
                    "LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                    "where allorder.create_time >=%s and allorder.create_time<=%s\n" +
                    "and esi.study_aim=1",bTime,tTime);
        }
        if (!teacherType.equals("不限")) {
            if (teacherType.equals("菲律宾")) {
                sql = sql + "\n" + "and allorder.tch_from=1";
            } else {
                sql = sql + "\n" + "and allorder.tch_from=2";
            }
        }
        if (!classType.equals("不限")) {
            if (classType.equals("套餐课")) {
                sql = sql + "\n" + "and (allorder.combo_name not like '%自由%' or allorder.combo_name not like '%自由%')" ;
            } else {
                sql = sql + "\n" + "and  (allorder.combo_name like '%自由%' or allorder.combo_name like '%自由%')";
            }
        }
        if (!renewType.equals("不限")) {
            if (renewType.equals("补升")) {
                sql = sql + "\n" + "and allorder.up!=0";
            } else {
                    sql = sql + "\n" + "and allorder.up=0";
            }
        }
        if(look.equals("table1")){
            sql = sql + "\n" + "group by es.id,allorder.id,money";
        }else{
            sql=sql+"\n"+"and es.acoin<1500";
            sql = sql + "\n" + "group by es.id,allorder.id";
        }
        workRenewSql=sql;
        if(look.equals("table1")){
            TableQuery query = new TableQuery(entityManager, WorkRenew.class, criterias, sql);
            DataSet<WorkRenew> actions=query.getResultDataSet();
            return actions;
        }else {
            TableQuery query = new TableQuery(entityManager, WorkRenewLessAcoin.class, criterias, sql);
            DataSet<WorkRenewLessAcoin> actions=query.getResultDataSet();
            return actions;
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getWorkRenewCnt(){
        ArrayList wholeCnt=new ArrayList();
        Double cnt1 = null;
        Double cnt2 = null;
        DecimalFormat df = new DecimalFormat("######0.00");
        String sql=workRenewWholeSql+"\n"+"group by es.id,allorder.combo_name,money";
        String sql0=String.format("select count(DISTINCT result.id) from (%s) result",sql);
        Query q0 = entityManager.createNativeQuery(sql0);
        List list0 = q0.getResultList();
        wholeCnt.add(list0.get(0).toString());

        String sql1=String.format("select count(DISTINCT result.id) from (%s) result where result.type=0",sql);
        Query q1 = entityManager.createNativeQuery(sql1);
        List list1 = q1.getResultList();
        cnt1= Double.valueOf(list1.get(0).toString());
        wholeCnt.add(list1.get(0).toString());

        String sql2=String.format("select count(DISTINCT result.id) from (%s) result where result.type!=0",sql);
        Query q2 = entityManager.createNativeQuery(sql2);
        List list2 = q2.getResultList();
        cnt2= Double.valueOf(list2.get(0).toString());
        wholeCnt.add(list2.get(0).toString());

        String sql3=String.format("select sum(result.money) from (%s) result",sql);
        Query q3 = entityManager.createNativeQuery(sql3);
        List list3 = q3.getResultList();
        wholeCnt.add(list3.get(0).toString());

        String sql4=String.format("select sum(result.money) from (%s) result where result.type=0",sql);
        Query q4 = entityManager.createNativeQuery(sql4);
        List list4 = q4.getResultList();
        wholeCnt.add(list4.get(0).toString());

        String sql5=String.format("select sum(result.money) from (%s) result where result.type!=0",sql);
        Query q5 = entityManager.createNativeQuery(sql5);
        List list5 = q5.getResultList();
        wholeCnt.add(list5.get(0).toString());

        String sql6=workRenewWholeSql.replace("and allorder.payed=1 and allorder.order_flag=2","\n"+"and es.acoin<1500"+"\n"+"group by es.id,allorder.id");
        sql6=String.format("select count(DISTINCT result.id) from (%s) result where result.type=0",sql6);
        Query q6 = entityManager.createNativeQuery(sql6);
        List list6 = q6.getResultList();
        Double data6;
        if(cnt1!=null){
            data6=cnt1/Long.parseLong(list6.get(0).toString())*100;
        }else{
            data6= 0d;
        }
        wholeCnt.add(df.format(data6));

        String sql7=workRenewWholeSql.replace("allorder.order_flag=2","allorder.order_flag=1");
        sql7=sql7+"\n"+"group by es.id,allorder.id";
        sql7=String.format("select count(DISTINCT result.id) from (%s) result where result.type!=0",sql7);
        Query q7 = entityManager.createNativeQuery(sql7);
        List list7 = q7.getResultList();
        Double data7;
        if(cnt2!=null){
            data7=cnt2/Long.parseLong(list7.get(0).toString())*100;
        }else{
            data7= 0d;
        }
        wholeCnt.add(df.format(data7));
        System.out.println(wholeCnt);
        return wholeCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkRefunds> getWorkRefunds(String data,DatatablesCriterias criterias){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String teacherType=cutData[2];
        String classType=cutData[3];
        String level=cutData[4];
        String age=cutData[5];
        String sql=String.format("select es.id as id,er.money as money,IFNULL(concat(es.level,es.sub_level),'UNKNOW') as rank,IFNULL(ecr.cnt,'0') as cost,esi.age_duration as age,allorder.combo_name as combo,er.reason as reason from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN (select eao.create_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                "group by eaod.order_id,eaod.begin_time\n" +
                "union\n" +
                "select eao.create_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n" +
                "LEFT JOIN ebk_refunds er on er.sid=es.id\n" +
                "LEFT JOIN (select sid,count(id) as cnt from ebk_class_records group by sid) ecr on ecr.sid=es.id\n" +
                "where er.refund_time>=%s and er.refund_time<=%s\n" +
                "and esi.study_aim=1 and er.status=3" ,bTime,tTime);
        if (!teacherType.equals("不限")) {
            if (teacherType.equals("菲律宾")) {
                sql = sql + "\n" + "and allorder.tch_from=1";
            } else {
                sql = sql + "\n" + "and allorder.tch_from=2";
            }
        }
        if (!classType.equals("不限")) {
            if (classType.equals("套餐课")) {
                sql = sql + "\n" + "and (allorder.combo_name not like '%自由%' or allorder.combo_name not like '%自由%')" ;
            } else {
                sql = sql + "\n" + "and  (allorder.combo_name like '%自由%' or allorder.combo_name like '%自由%')";
            }
        }
        if(!level.equals("ALL")){
            sql=sql+"\n"+"and concat(es.level,es.sub_level)='"+level+"'";
        }
        if(!age.equals("allAge")){
            if(age.equals("0")){
                sql=sql+"\nand esi.age_duration not in(1,2,3,10,11,12,6,7,8,9)";
            }else{
                sql=sql+"\nand esi.age_duration="+age;
            }
        }
        sql=sql+"\ngroup by es.id,er.id";
        TableQuery query = new TableQuery(entityManager, WorkRefunds.class, criterias, sql);
        DataSet<WorkRefunds> actions=query.getResultDataSet();
        workRefundsCnt.add(query.getTotalCount().toString());
        workRefundsSql=sql;
        String sumSql=String.format("select ROUND(sum(result.money),2) from (%s) result",sql);
        Query q = entityManager.createNativeQuery(sumSql);
        List list = q.getResultList();
        workRefundsCnt.add(list.get(0));
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkClassMessage> getWorkClassMessage(String data,DatatablesCriterias criterias){
        String[]cutData=data.split("\\+");
        String bTime =cutData[0];
        String tTime=cutData[1];
        String classType=cutData[2];
        String show=cutData[3];
        String sql=String.format("select ecr.begin_time as time,ecr.id as cid,es.id as sid,es.nickname as sname,et.id as tid,et.nickname as tname,ifNULL(eru.nickname,'无导师') as ghs from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on es.id=esi.sid\n" +
                "LEFT JOIN ebk_class_records ecr on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_teachers et on ecr.tid=et.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                "LEFT JOIN ebk_class_comments ecc on ecc.cid=ecr.id \n"+
                "where ecr.begin_time>%s and ecr.begin_time<%s",bTime,tTime);
        if(!classType.equals("不限")){
            if(classType.equals("菲律宾")){
                sql=sql+"\n"+"and ecr.catalog=1";
            }else{
                sql=sql+"\n"+"and ecr.catalog=2";
            }
        }
        switch (show){
            case "差评数":sql=sql+"\n"+"and ecc.choice=-1";break;
            case "缺席的课时数":sql=sql+"\n"+"and ecr.status=5";break;
            case "测评课完成数":sql=sql+"\n"+"and ecr.status=3 and ecr.free_try=2";break;
        }
        sql=sql+"\n"+"group by es.id,ecr.id";
        TableQuery query = new TableQuery(entityManager, WorkClassMessage.class, criterias, sql);
        DataSet<WorkClassMessage> actions=query.getResultDataSet();
        workClassMessageCnt=query.getTotalCount();
        workClassMessageSql=sql;
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "day_student_activity_chart", keyGenerator = "wiselyKeyGenerator")
    public LinkedHashMap<String,String> getDayStudentActivityChart(String data){
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String sql=String.format("select UNIX_TIMESTAMP(time),count(id) from ABC360_WEB_STUDENT_ACTION_LAST_MONTH_WITH_TODAY_TBL wsal \n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=wsal.operatorid\n" +
                "where UNIX_TIMESTAMP(time) BETWEEN %s and (%s+86400)\n" +
                "and esi.study_aim=1\n" +
                "group by DATE_FORMAT(wsal.time, '%%Y-%%m-%%d %%H:%%i')",data,data);
        Query q = firstEntityManager.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[0].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getCostClassCnt() {
        return costClassCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getCostSaClassCnt() {
        return costSaClassCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getNewStudentCnt() {
        return newStudentCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getOldStudentCnt() {
        return oldStudentCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getWholeSql() {
        return wholeSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getWholeSaSql() {
        return wholeSaSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getNewStudentSql() {
        return newStudentSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getOldStudentSql() {
        return oldStudentSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getNewStudentPayCnt() {
        return newStudentPayCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getOldStudentPayCnt() {
        return oldStudentPayCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getWorkStudentMessageSql(){return workStudentMessageSql;};

    @Override
    @SuppressWarnings("unchecked")
    public Long getWorkStudentMessageCnt(){return workStudentMessageCnt;};

    @Override
    @SuppressWarnings("unchecked")
    public String getWorkClassMessageSql(){return workClassMessageSql;};

    @Override
    @SuppressWarnings("unchecked")
    public Long getWorkClassMessageCnt(){return workClassMessageCnt;};

    @Override
    @SuppressWarnings("unchecked")
    public String getWorkRenewSql(){return workRenewSql;}

    @Override
    @SuppressWarnings("unchecked")
    public String getWorkRefundsSql(){
        return workRefundsSql;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getWorkRefundsCnt(){
        return workRefundsCnt;
    }


}
