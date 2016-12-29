package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.repositories.qingshao.EbkStudentsRepository;
import com.qa.data.visualization.repositories.qingshao.EbkTeachersRepository;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dykj on 2016/11/17.
 */
@Service
public class ManageClassServiceImpl implements ManageClassService {
    private Long costClassCnt;
    private String wholeSql;
    private Long costSaClassCnt;
    private String wholeSaSql;
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
        ebkTeachersList = ebkTeachersRepository.getEbkTeachersById(query, top10);
        if(ebkTeachersList.size()==0){
            ebkTeachersList = ebkTeachersRepository.getEbkTeachersByEmail(query, top10);
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
    public DataSet<ManagerCostClass> getCostClass(String data, DatatablesCriterias criterias) throws ParseException {
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
                        aSql=aSql+ "\n LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                                "group by eaod.order_id,eaod.begin_time\n" +
                                "union\n" +
                                "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
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
                        aSql=aSql+ "\n LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                                "group by eaod.order_id,eaod.begin_time\n" +
                                "union\n" +
                                "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
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
        TableQuery query = new TableQuery(entityManager, ManagerCostClass.class, criterias, sql);
        DataSet<ManagerCostClass> result = query.getResultDataSet();
        costClassCnt = query.getTotalCount();
        wholeSql = sql;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<ManagerCostSaClass> getCostSaClass(String data, DatatablesCriterias criterias) throws ParseException {
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
            sql = sql + "\n" + "and (ecr.status!=-1)";
        } else {
            switch (classStatus) {
                case "res":
                    sql = sql + "\n" + "and (ecr.status=9)";
                    break;
                case "顺利结束":
                    sql = sql + "\n" + "and (ecr.status=3)";
                    break;
            }
        }
        sql=sql+"\n"+"group by ecr.sid,ecr.begin_time";
        TableQuery query = new TableQuery(entityManager, ManagerCostSaClass.class, criterias, sql);
        DataSet<ManagerCostSaClass> result = query.getResultDataSet();
        costSaClassCnt = query.getTotalCount();
        wholeSaSql = sql;
        return result;
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
    public Long getCostClassCnt() {
        return costClassCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getCostSaClassCnt() {
        return costSaClassCnt;
    }

}
