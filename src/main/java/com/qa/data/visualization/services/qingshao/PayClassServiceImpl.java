package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.payStudent;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dykj on 2016/12/26.
 */
@Service
public class PayClassServiceImpl implements PayClassService {
    private Long newStudentCnt;
    private String newStudentSql;
    private String newStudentPayCnt;
    private Long oldStudentCnt;
    private String oldStudentSql;
    private String oldStudentPayCnt;
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager firstEntityManager;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;
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
    public Long getNewStudentCnt() {
        return newStudentCnt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getOldStudentCnt() {
        return oldStudentCnt;
    }
}
