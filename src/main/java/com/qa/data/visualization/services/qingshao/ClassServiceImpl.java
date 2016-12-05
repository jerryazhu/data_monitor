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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Query q = entityManager.createNativeQuery("select distinct ebk_teacher_group.title\n" +
                "from ebk_teachers inner join ebk_teacher_group on ebk_teachers.workgroup = ebk_teacher_group.id\n" +
                "where ebk_teachers.tch_course = 1 and ebk_teachers.is_tester=0 order by title ");
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
        Query q = entityManager.createNativeQuery("select distinct ebk_teacher_group.title\n" +
                "from ebk_teachers inner join ebk_teacher_group on ebk_teachers.workgroup = ebk_teacher_group.id\n" +
                "where ebk_teachers.tch_course = 1 and ebk_teachers.is_tester=1 order by title");
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
        String sql = "select ecr.begin_time as begin_time,ecr.tid as tid,ecr.tname as tname,ecr.sid as sid,ecr.sname as sname  \n" +
                "from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid = es.id\n" +
                "LEFT JOIN ebk_student_info esi on ecr.sid = esi.sid\n" +
                "LEFT JOIN ebk_teachers et on ecr.tid = et.id\n" +
                "LEFT JOIN ebk_teacher_group etg on et.workgroup = etg.id\n" +
                "where" ;
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
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "ecr.begin_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "ecr.begin_time>=" + bTime + " and ecr.begin_time<=" + tTime;
        }
        sql=sql+"\n"+"and ecr.status=3\n" +"and esi.study_aim=1 and ecr.free_try=0";
        if (!studentMessage.equals("all")) {
            sql = sql + "\n" + "and es.id= " + studentMessage;
        }
        if (comboCountry.equals("不限")) {
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
        } else {
            if (comboCountry.equals("菲律宾")) {
                switch (combo) {
                    case "不限":
                        sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                        break;
                    case "51":
                        sql = sql + "\n" + "and (es.lsns_per_day=1 and es.days_per_week=5)";
                        break;
                    case "52":
                        sql = sql + "\n" + "and (es.lsns_per_day=2 and es.days_per_week=5)";
                        break;
                }
            } else {
                switch (combo) {
                    case "不限":
                        sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                        break;
                    case "51":
                        sql = sql + "\n" + "and (es.lsns_per_day_eu=1 and es.days_per_week_eu=5)";
                        break;
                    case "52":
                        sql = sql + "\n" + "and (es.lsns_per_day_eu=2 and es.days_per_week_eu=5)";
                        break;
                    case "31":
                        sql = sql + "\n" + "and (es.lsns_per_day_eu=1 and es.days_per_week_eu=3)";
                        break;
                    case "32":
                        sql = sql + "\n" + "and (es.lsns_per_day_eu=2 and es.days_per_week_eu=3)";
                        break;
                }
            }
        }
        if (!binding.equals("不限")) {
            switch (binding) {
                case "绑定":
                    sql = sql + "\n" + "and ecr.is_bind=1";
                    break;
                case "不绑定":
                    sql = sql + "\n" + "and ecr.is_bind=-1";
                    break;
            }
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
        String sql = "select es.create_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ifnull(sum(eao.tmoney),0) as cnt from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                "LEFT JOIN ebk_advertisement_source eas on eas.id=esi.knowus\n" +
                "LEFT JOIN ebk_acoin_orders eao on es.id = eao.sid\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                "where ";
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
        sql = sql + "\n" + "group by es.id";
        TableQuery query = new TableQuery(entityManager, payStudent.class, criterias, sql);
        DataSet<payStudent> result = query.getResultDataSet();
        newStudentCnt = query.getTotalCount();
        newStudentSql = sql;
        if (tableShow.equals("销售量及购买人数")) {
            Query q = entityManager.createNativeQuery("select sum(result.cnt) from (" + sql + ") result");
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
        String sql = "select eao.create_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ifnull(sum(eao.tmoney),0) as cnt \n" +
                "from ebk_students es\n" +
                "LEFT JOIN ebk_acoin_orders eao on eao.sid=es.id\n" +
                "LEFT JOIN ebk_acoin_order_detail eaod on eaod.order_id=eao.id\n" +
                "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                "where ";
        String[] cutData = data.split("\\+");
        String bTime = cutData[0];
        String tTime = cutData[1];
        String teacherType = cutData[2];
        String classType = cutData[3];
        String studentStatus = cutData[4];
        String classShowType = cutData[5];
        if (bTime.equals("all") || tTime.equals("all")) {
            sql = sql + "\n" + "eao.create_time>" + yesterdayUnix;
        } else {
            sql = sql + "\n" + "eao.create_time >=" + bTime + " and eao.create_time<=" + tTime;
        }
        sql=sql+"\n"+"and esi.study_aim=1 \n"+"and eao.payed=1";
        if (!teacherType.equals("不限")) {
            if (teacherType.equals("菲律宾")) {
                sql = sql + "\n" + "and eaod.tch_from=1";
            } else {
                sql = sql + "\n" + "and eaod.tch_from=2";
            }
        }
        if (!classType.equals("不限")) {
            if (classType.equals("套餐课")) {
                sql = sql + "\n" + "and eaod.combo_name!='自由套餐'";
            } else {
                sql = sql + "\n" + "and eaod.combo_name='自由套餐'";
            }
        }
        if (!studentStatus.equals("不限")) {
            if (studentStatus.equals("上课中学员")) {
                sql = sql + "\n" + "and es.status=1";
            } else {
                sql = sql + "\n" + "and es.status=4";
            }
        }
        if (!classShowType.equals("总人数")) {
            if (classShowType.equals("补升人数和金额")) {
                sql = sql + "\n" + "and eao.upgrade_from!=0";
            } else {
                sql = sql + "\n" + "and eao.upgrade_from=0";
            }
        }
        sql = sql + "\n" + "group by es.id";
        TableQuery query = new TableQuery(entityManager, payStudent.class, criterias, sql);
        DataSet<payStudent> result = query.getResultDataSet();
        oldStudentCnt = query.getTotalCount();
        oldStudentSql = sql;
        Query q = entityManager.createNativeQuery("select sum(result.cnt) from (" + sql + ") result");
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
}
