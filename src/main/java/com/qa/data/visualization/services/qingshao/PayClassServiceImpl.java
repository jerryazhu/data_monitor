package com.qa.data.visualization.services.qingshao;

import com.mysql.fabric.xmlrpc.base.Data;
import com.qa.data.visualization.entities.qingshao.*;
import com.qa.data.visualization.repositories.qingshao.EbkCcRepository;
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
import java.util.LinkedHashMap;
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
    private String ccSaleMessageSql;
    private Long ccSaleMessageCnt;
    private String ccSaleMessageMoneyCnt;
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager firstEntityManager;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;
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
                "LEFT JOIN ebk_crm_group ecg on ecg.id=ecgu.group_id\n" +
                "LEFT JOIN ebk_acoin_orders eao on eao.sale_adviser=eru.id\n" +
                "where ecg.direction=1 and ecg.role=1 and eao.payed=1 and eru.status=1\n" +
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
        String table=cutData[4];
        String saleType=cutData[5];
        String sql=String.format("select es.id as sid,es.nickname as sname,eru.id as ccid,eru.nickname as ccname,ecg.title as ccgroup,SUM(eao.tmoney) as money from ebk_acoin_orders eao\n" +
                "LEFT JOIN ebk_students es on eao.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eao.sale_adviser=eru.id\n" +
                "LEFT JOIN ebk_crm_group_user ecgu on ecgu.user_id=eru.id\n" +
                "LEFT JOIN ebk_crm_group ecg on ecg.id=ecgu.group_id\n" +
                "LEFT JOIN ebk_crm_adviser_public_store ecaps on ecaps.sid=eao.sid \n "+
                "where eao.pay_time>=%s and eao.pay_time<=%s\n" +
                "and ecg.direction=1 and ecg.role=1 and eao.payed=1 and eru.status=1",bTime,tTime);
        if(!ccMessage.equals("all")){
            sql=sql+"\n"+"and eao.sale_adviser="+ccMessage;
        }
        if(!ccGroup.equals("Group")){
            sql=sql+"\n"+"and ecg.title='"+ccGroup+"'";
        }
        if(table.equals("退款")){
            sql=sql+"\n"+"and eao.refunded=1";
        }else{
            switch (saleType){
                case "新分会员":sql=sql+"\n"+"and (ecaps.adv_group is null or ecaps.adv_group!=1)";break;
                case "转介绍会员":sql=sql+"\n"+"and eao.order_type=2";break;
                case "公库会员":sql=sql+"\n"+"and ecaps.adv_group=1";break;
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
    public ArrayList getCcGroup(){
        ArrayList result=new ArrayList();
        String sql="select title from ebk_crm_group where direction=1 and role=1";
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
