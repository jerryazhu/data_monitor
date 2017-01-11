package com.qa.data.visualization.services.qingshao;

import com.mysql.fabric.xmlrpc.base.Data;
import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.repositories.qingshao.EbkCcRepository;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableConvert;
import com.web.spring.datatable.TableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.AbstractEntityInformation;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private String ccSaleMessageSql;
    private Long ccSaleMessageCnt;
    private String ccSaleMessageMoneyCnt;
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager firstEntityManager;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;
    @PersistenceContext(unitName = "thirdPersistenceUnit")
    private EntityManager callCenterEntityManager;
    @Autowired
    private EbkCcRepository ebkCcRepository;
    @Override
    @SuppressWarnings("unchecked")
    public DataSet<PayStudent> getNewStudent(String data, DatatablesCriterias criterias) throws ParseException {
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
        TableQuery query = new TableQuery(entityManager, PayStudent.class, criterias, sql);
        DataSet<PayStudent> result = query.getResultDataSet();
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
    public DataSet<PayStudent> getOldStudent(String data, DatatablesCriterias criterias) throws ParseException {
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
                    "LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
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
            sql = String.format("select allorder.pay_time as create_time,es.id as sid,es.nickname as sname,es.status as status,ifNUll(eru.nickname,'UNKNOWN') as cc, ROUND(ifnull(allorder.tmoney,0),2) as cnt \n" +
                    "from ebk_students es\n" +
                    "LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "LEFT JOIN ebk_rbac_user eru on eru.id=es.adviser\n" +
                    "where ");
            if (bTime.equals("all") || tTime.equals("all")) {
                sql = sql + "\n" + "allorder.pay_time>" + yesterdayUnix;
            } else {
                sql = sql + "\n" + "allorder.pay_time >=" + bTime + " and allorder.pay_time<=" + tTime;
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
        TableQuery query = new TableQuery(entityManager, PayStudent.class, criterias, sql);
        DataSet<PayStudent> result = query.getResultDataSet();
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
    public DataSet<PayCCMessage> getPayCCMessage(DatatablesCriterias criterias){
        String sql="select result.id as id,result.name as name,result.title as title,result.time as time,result.time as workage,max(result.money) as max,min(result.money) as min,avg(result.money) as avg,sum(result.money) as sum from \n" +
                "(select eru.id as id,eru.nickname as name,ecg.title as title,eru.create_time as time,from_unixtime(eao.pay_time, '%Y-%m') as month,sum(eao.tmoney) as money from ebk_rbac_user eru \n" +
                "LEFT JOIN ebk_crm_group_user ecgu on ecgu.user_id=eru.id\n" +
                "LEFT JOIN ebk_crm_groups ecg on ecg.id=ecgu.group_id\n" +
                "LEFT JOIN ebk_acoin_orders eao on eao.sale_adviser=eru.id\n" +
                "where ecg.direction=1 and ecg.role=50 and eao.payed=1 and eru.status=1\n" +
                "group by eru.id,from_unixtime(eao.pay_time, '%Y-%m'))result\n" +
                "group by result.id";
        TableQuery query=new TableQuery(entityManager,PayCCMessage.class,criterias,sql);
        return query.getResultDataSet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<PayCCSaleMessage> getPayCcSaleMessage(String data,DatatablesCriterias criterias){
        String[] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String ccMessage=cutData[2];
        String ccGroup=cutData[3];
        String[] cutGroup=ccGroup.split(",");
        String table=cutData[4];
        String saleType=cutData[5];
        String sql=String.format("select es.id as sid,es.nickname as sname,eru.id as ccid,eru.nickname as ccname,ecg.title as ccgroup,SUM(eao.tmoney) as money from ebk_acoin_orders eao\n" +
                "LEFT JOIN ebk_students es on eao.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eao.sale_adviser=eru.id\n" +
                "LEFT JOIN ebk_crm_group_user ecgu on ecgu.user_id=eru.id\n" +
                "LEFT JOIN ebk_crm_groups ecg on ecg.id=ecgu.group_id\n" +
                "LEFT JOIN ebk_operate_record eor on eor.sid=eao.sid \n "+
                "where eao.pay_time>=%s and eao.pay_time<=%s\n" +
                "and ecg.direction=1 and ecg.role=50 and eao.payed=1 and eru.status=1",bTime,tTime);
        if(!ccMessage.equals("all")){
            sql=sql+"\n"+"and eao.sale_adviser="+ccMessage;
        }
        if (!ccGroup.equals("null")) {
            String groupSql="and (ecg.title='"+cutGroup[0]+"'";
            for(int i=1;i<cutGroup.length;i++){
                groupSql=groupSql+" or ecg.title='"+cutGroup[i]+"'";
            }
            groupSql=groupSql+")";
            sql = sql + "\n" +groupSql;
        }
        if(table.equals("退款")){
            sql=sql+"\n"+"and eao.refunded=1";
        }else{
            switch (saleType){
                case "新分会员":sql=sql+"\n"+"and (eor.adv_group is null or eor.adv_group!=1 or eor.type!=1)";break;
                case "转介绍会员":sql=sql+"\n"+"and eao.order_type=2";break;
                case "公库会员":sql=sql+"\n"+"and eor.type=1 and eor.adv_group=1";break;
                case "续费":sql=sql+"\n"+"and eao.order_flag=2";break;
            }
        }
        sql=sql+"\n"+"group by eao.sid,eao.sale_adviser";
        ccSaleMessageSql=sql;
        TableQuery query=new TableQuery(entityManager,PayCCSaleMessage.class,criterias,sql);
        DataSet<PayCCSaleMessage> action=query.getResultDataSet();
        ccSaleMessageCnt=query.getTotalCount();
        String cntSql=String.format("select sum(result.money) from (%s) as result",sql);
        Query q=entityManager.createNativeQuery(cntSql);
        List list=q.getResultList();
        ccSaleMessageMoneyCnt=list.get(0).toString();
        return action;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<PayCallPhone> getPayCallPhone(String data,DatatablesCriterias criterias) throws ParseException {
        String sql=String.format("select callagent,SUM(duration),SUM(billsec),count(id),sum(if(billsec>30,1,0))from agentcdr where calldate like '%s%%' group by callagent",data);
        Query q=callCenterEntityManager.createNativeQuery(sql);
        List<Object[]> number=q.getResultList();
        String sqlPerson="select eru.ccid,eru.nickname,ecg.title,eru.id from ebk_rbac_user eru\n" +
                "LEFT JOIN ebk_crm_group_user egu on egu.user_id=eru.id\n" +
                "LEFT JOIN ebk_crm_groups ecg on ecg.id=egu.group_id\n" +
                "where ecg.role=50 and ecg.direction=1";
        Query qP=entityManager.createNativeQuery(sqlPerson);
        List<Object[]> person=qP.getResultList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(data);
        long ts = date.getTime();
        String res = String.valueOf(ts/1000);
        String numberGo=String.format("select callagent,count(callagent),sum(if(billsec>30,1,0)) from (\n" +
               "select DISTINCT ag.callagent,ag.duration,ag.billsec from agentcdr ag \n" +
               "LEFT JOIN ebk_students es on es.mobile=ag.callnumber\n" +
               "LEFT JOIN ebk_operate_record eor on eor.sid=es.id\n" +
               "where ag.calldate like '%s%%'\n" +
               "and eor.type=1 and eor.adv_group=1 and eor.create_time<%s) re\n" +
               "group by callagent",data,res);
        Query qG=firstEntityManager.createNativeQuery(numberGo);
        List<Object[]> goNumber=qG.getResultList();
        ArrayList<PayCallPhone> resultRow=new ArrayList<PayCallPhone>();
        for (Object[] aNumber : number) {
            for (Object[] aPerson : person) {
                if (aNumber[0].toString().equals(aPerson[0].toString())){
                    PayCallPhone payCallPhone = new PayCallPhone();
                    payCallPhone.setCallid(aNumber[0].toString());
                    payCallPhone.setCcname(aPerson[1].toString());
                    payCallPhone.setCcgroup(aPerson[2].toString());
                    payCallPhone.setCcid(aPerson[3].toString());
                    payCallPhone.setCalltime(aNumber[1].toString());
                    payCallPhone.setCallreltime(aNumber[2].toString());
                    payCallPhone.setCallnumber(Integer.parseInt(aNumber[3].toString()));
                    payCallPhone.setCallrelnumber(Integer.parseInt(aNumber[4].toString()));
                    boolean find=false;
                    for (Object[] aGoNumber : goNumber) {
                        if (aNumber[0].toString().equals(aGoNumber[0].toString())){
                            payCallPhone.setCallnumber1(Integer.parseInt(aGoNumber[1].toString()));
                            payCallPhone.setCallnumber2(Integer.parseInt(aNumber[3].toString())-Integer.parseInt(aGoNumber[1].toString()));
                            payCallPhone.setCallrelnumber1(Integer.parseInt(aGoNumber[2].toString()));
                            payCallPhone.setCallrelnumber2(Integer.parseInt(aNumber[4].toString())-Integer.parseInt(aGoNumber[2].toString()));
                            find=true;
                            break;
                        }
                    }
                    if(!find){
                        payCallPhone.setCallnumber1(0);
                        payCallPhone.setCallnumber2(Integer.parseInt(aNumber[3].toString()));
                        payCallPhone.setCallrelnumber1(0);
                        payCallPhone.setCallrelnumber2(Integer.parseInt(aNumber[4].toString()));
                    }
                    resultRow.add(payCallPhone);
                    break;
                }
            }
        }
        TableConvert tableConvert = new TableConvert(resultRow,criterias);
        DataSet<PayCallPhone> actions=tableConvert.getResultDataSet();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<PayPercentConversion> getPayPercentConversion(String data,DatatablesCriterias criterias){
        String []cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String ccMessage=cutData[2];
        String group=cutData[3];
        String[] cutGroup=group.split(",");
        String allSmallSql=String.format( "select DISTINCT eor.user_id,eru.nickname,ecg.title,eor.sid from ebk_operate_record eor \n" +
                "LEFT JOIN ebk_crm_groups ecg on eor.user_group=ecg.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=eor.user_id\n" +
                "where eor.create_time >=%s  and eor.create_time <=%s\n" +
                "and eor.type=0 and eor.adv_group=1 \n" +
                "and ecg.role=50 and ecg.direction=1 and eor.operator_name='system'",bTime,tTime);
        if(!ccMessage.equals("all")){
            allSmallSql=allSmallSql+"\n"+"and eor.user_id="+ccMessage;
        }
        if (!group.equals("null")) {
            String groupSql="and (ecg.title='"+cutGroup[0]+"'";
            for(int i=1;i<cutGroup.length;i++){
                groupSql=groupSql+" or ecg.title='"+cutGroup[i]+"'";
            }
            groupSql=groupSql+")";
            allSmallSql = allSmallSql + "\n" +groupSql;
        }
        String allSql=String.format("select user_id,nickname,title,COUNT(sid)from(%s) re\n" +
                "group by user_id",allSmallSql);
        Query qAll=entityManager.createNativeQuery(allSql);
        List<Object[]> listAll=qAll.getResultList();
        String buySql=String.format("select user_id,COUNT(DISTINCT sid),SUM(tmoney),count(id) from(\n" +
                "select DISTINCT eor.user_id,eor.sid,eao.tmoney,eao.id from ebk_operate_record eor \n" +
                "LEFT JOIN ebk_acoin_orders eao on eor.user_id=eao.sale_adviser\n" +
                "LEFT JOIN ebk_crm_groups ecg on eor.user_group=ecg.id\n" +
                "where eor.create_time >=%s  and eor.create_time <=%s\n" +
                "and eao.create_time >=%s  and eao.create_time <=%s\n" +
                "and eor.sid=eao.sid and eao.sale_adviser=eor.user_id and eor.type=0 and eor.adv_group=1 and eao.payed=1\n" +
                "and eao.order_flag=1 and ecg.role=50 and ecg.direction=1 and eor.operator_name='system'\n" +
                ") re\n" +
                "group by user_id",bTime,tTime,bTime,tTime);
        Query qBuy=entityManager.createNativeQuery(buySql);
        List<Object[]> listBuy=qBuy.getResultList();
        String testSql=String.format("select user_id,COUNT(DISTINCT sid),sum(if(status=3,1,0)) from(\n" +
                "select DISTINCT eor.user_id,eor.sid,ecr.status from ebk_operate_record eor \n" +
                "LEFT JOIN ebk_crm_groups ecg on eor.user_group=ecg.id\n" +
                "LEFT JOIN ebk_class_records ecr on ecr.sid=eor.sid\n" +
                "where eor.create_time >=%s  and eor.create_time <=%s\n" +
                "and ecr.begin_time>=%s and ecr.begin_time<=%s\n" +
                "and ecr.sid=eor.sid and ecr.free_try=2 and eor.type=0 and eor.adv_group=1\n" +
                "and ecg.role=50 and ecg.direction=1 and eor.operator_name='system') re \n" +
                "group by user_id",bTime,tTime,bTime,tTime);
        Query qTest=entityManager.createNativeQuery(testSql);
        List<Object[]> listTest=qTest.getResultList();
        ArrayList<PayPercentConversion> resultRows=new ArrayList<PayPercentConversion>();
        for(Object[] aListAll:listAll){
            PayPercentConversion payPercentConversion=new PayPercentConversion();
            payPercentConversion.setCcid(aListAll[0].toString());
            payPercentConversion.setCcname(aListAll[1].toString());
            payPercentConversion.setCcgroup(aListAll[2].toString());
            boolean buy=false;
            for(Object[] aListBuy:listBuy){
                if(aListAll[0].equals(aListBuy[0])){
                    payPercentConversion.setAllpercent(Double.parseDouble(aListBuy[1].toString())/Double.parseDouble(aListAll[3].toString())*100);
                    payPercentConversion.setPersonpercent(Double.parseDouble(aListBuy[2].toString())/Double.parseDouble(aListAll[3].toString()));
                    if(!aListBuy[3].toString().equals("0")){
                        payPercentConversion.setOnepercent(Double.parseDouble(aListBuy[2].toString())/Double.parseDouble(aListBuy[3].toString()));
                    }else{
                        payPercentConversion.setOnepercent(0);
                    }
                    buy=true;
                    break;
                }
            }
            if(!buy){
                payPercentConversion.setAllpercent(0);
                payPercentConversion.setPersonpercent(0);
                payPercentConversion.setOnepercent(0);
            }
            boolean test=false;
            for(Object[] aListTest:listTest){
                if(aListAll[0].equals(aListTest[0])){
                    payPercentConversion.setTestpercent(Double.parseDouble(aListTest[1].toString())/Double.parseDouble(aListAll[3].toString())*100);
                    boolean buyClass=false;
                    for(Object[] aListBuy:listBuy){
                        if(aListBuy[0].equals(aListTest[0])){
                            payPercentConversion.setBuypercent(Double.parseDouble(aListBuy[1].toString())/Double.parseDouble(aListTest[2].toString())*100);
                            buyClass=true;
                        }
                    }
                    if(!buyClass){
                        payPercentConversion.setBuypercent(0);
                    }
                    test=true;
                    break;
                }
            }
            if(!test){
                payPercentConversion.setTestpercent(0);
            }
            resultRows.add(payPercentConversion);
        }
        TableConvert tableConvert = new TableConvert(resultRows,criterias);
        DataSet<PayPercentConversion> actions=tableConvert.getResultDataSet();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getClassMemo(String data){
        ArrayList result=new ArrayList();
        int s=Integer.parseInt(data)/1000000;
        String sql=String.format("select submit_memo_time,memo_words,memo_phrases from ebk_class_memo_%s where cid=%s",s,data);
        Query q = entityManager.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        if(list.size()==0){
            result.add("无备注");
        }
        for(Object[] objects:list){
            result.add(objects[0].toString());
            result.add(objects[1].toString());
            result.add(objects[2].toString());
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getCcGroup(){
        ArrayList result=new ArrayList();
        String sql="select title from ebk_crm_groups where direction=1 and role=50";
        Query q=entityManager.createNativeQuery(sql);
        List list=q.getResultList();
        for(Object aList:list){
            result.add(aList);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AutoComplete> getCcAutoComplete(String query){
        List<AutoComplete> autoCompleteList = new ArrayList<>();
        Pageable top10 = new PageRequest(0, 10);
        List<EbkCC> ebkCcList;
        ebkCcList = ebkCcRepository.getEbkCcById(query, top10);
        if(ebkCcList.size()==0){
            ebkCcList = ebkCcRepository.getEbkCcByName(query, top10);
        }
        for (EbkCC ebkCC : ebkCcList) {
            AutoComplete autoComplete = new AutoComplete();
            autoComplete.setValue(String.valueOf(ebkCC.getId()));
            autoComplete.setData(ebkCC.getNickname());
            autoCompleteList.add(autoComplete);
        }
        return autoCompleteList;
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
    public String getCcSaleMessageSql(){
        return ccSaleMessageSql;
    }
    @Override
    @SuppressWarnings("unchecked")
    public Long getCcSaleMessageCnt(){
        return ccSaleMessageCnt;
    }
    @Override
    @SuppressWarnings("unchecked")
    public String getCcSaleMessageMoneyCnt(){
        return ccSaleMessageMoneyCnt;
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
