package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableConvert;
import com.web.spring.datatable.TableQuery;
import org.springframework.cache.annotation.Cacheable;
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
 * Created by dykj on 2016/12/26.
 */
@Service
public class WorkClassServiceImpl implements WorkClassService{
    private String workStudentMessageSql;
    private Long workStudentMessageCnt;
    private Long workClassMessageCnt;
    private String workClassMessageSql;
    private String workRenewSql;
    private String workRenewWholeSql;
    private String workRefundsSql;
    private String workLoseClassSql;
    private Long workLoseClassCnt;
    private ArrayList workRefundsCnt=new ArrayList();
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager firstEntityManager;
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;
    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkStudentMessage> getWorkStudentMessage(String data, DatatablesCriterias criterias) throws ParseException {
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
                "LEFT JOIN(select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eaod.begin_time,eao.pay_time from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                "group by eaod.order_id,eaod.begin_time\n" +
                "union\n" +
                "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,easo.begin_time,eao.pay_time from ebk_acoin_orders eao\n" +
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
    public DataSet<WorkLoseStudentClass> getWorkLoseStudentClass(String data, DatatablesCriterias criterias){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String combo=cutData[2];
        String sql=String.format("select es.id as id,es.nickname as name,ifNUll(eru.nickname,'UNKNOWN') as ghs,group_concat(ecr.begin_time order by ecr.begin_time asc) as day from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_class_records ecr on ecr.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                "where ecr.begin_time>=%s and ecr.begin_time<=%s\n" +
                "and esi.study_aim=1 and es.status=1",bTime,tTime);
        if(!combo.equals("不限")){
            switch (combo) {
                case "菲律宾套餐":
                    sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                    break;
                case "欧美套餐":
                    sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                    break;
                case "自由课套餐":
                    sql = sql + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                    break;
            }
        }
        sql=sql+"\n"+"group by es.id";
        workLoseClassSql=sql;
        Query q = entityManager.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        ArrayList days=new ArrayList();
        ArrayList ids=new ArrayList();
        ArrayList ghs=new ArrayList();
        ArrayList names=new ArrayList();
        for(Object[] result:list){
            ids.add(result[0]);
            names.add(result[1]);
            ghs.add(result[2]);
            String allDays=bTime+","+result[3]+","+tTime;
            int day=0;
            String[] cutDays=allDays.split(",");
            for(int i=0;i<(cutDays.length-1);i++){
                int differ=(Integer.parseInt(cutDays[i+1])-Integer.parseInt(cutDays[i]))/(3600*24);
                if(differ>day&&differ<32){
                    day=differ;
                }
            }
            days.add(day);
        }
        List<WorkLoseStudentClass> resultRow = new ArrayList<>();
        Map<String,HashMap> map=new HashMap<String,HashMap>();
        for(int i=0;i<ids.size();i++){
            HashMap mapSmall=new HashMap();
            mapSmall.put("ids",ids.get(i).toString());
            mapSmall.put("names",names.get(i).toString());
            mapSmall.put("ghs",ghs.get(i).toString());
            mapSmall.put("days", days.get(i));
            map.put(ids.get(i).toString(),mapSmall);
        }
        for (Object id : ids) {
            WorkLoseStudentClass object=new WorkLoseStudentClass();
            object.setId(map.get(id.toString()).get("ids").toString());
            object.setName(map.get(id.toString()).get("names").toString());
            object.setGhs(map.get(id.toString()).get("ghs").toString());
            object.setDay((Integer) map.get(id.toString()).get("days"));
            resultRow.add(object);
        }
        TableConvert tableConvert = new TableConvert(resultRow,criterias);
        DataSet<WorkLoseStudentClass> actions=tableConvert.getResultDataSet();
        workLoseClassCnt=tableConvert.getTotalCount();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkLoseStudentAcoin> getWorkLoseStudentAcoin(String data, DatatablesCriterias criterias){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String combo=cutData[2];
        String sql=String.format("select es.id as id,es.nickname as name,ifNUll(eru.nickname,'UNKNOWN') as ghs,eail.create_time as day,eail.acoin as acoin from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_acoin_io_log eail on eail.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                "where eail.create_time>=%s and eail.create_time<=%s\n" +
                "and eail.acoin_io_type=22 and esi.study_aim=1 and es.status=1",bTime,tTime);
        if(!combo.equals("不限")){
            switch (combo) {
                case "菲律宾套餐":
                    sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                    break;
                case "欧美套餐":
                    sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                    break;
                case "自由课套餐":
                    sql = sql;
                    break;
            }
        }
        sql=sql+"\n"+"group by es.id,day"+"\n"+"order by day";
        workLoseClassSql=sql;
        TableQuery query = new TableQuery(entityManager, WorkLoseStudentAcoin.class, criterias, sql);
        DataSet<WorkLoseStudentAcoin> actions=query.getResultDataSet();
        return actions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getStudentLoseDay(String data){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String combo=cutData[2];
        String sid=cutData[3];
        String sql=String.format("select ecr.begin_time as day from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_class_records ecr on ecr.sid=es.id\n" +
                "where ecr.begin_time>=%s and ecr.begin_time<=%s\n" +
                "and es.status=1 and esi.study_aim=1\n" +
                "and es.id=%s",bTime,tTime,sid);
        if(!combo.equals("不限")){
            switch (combo) {
                case "菲律宾套餐":
                    sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                    break;
                case "欧美套餐":
                    sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                    break;
                case "自由课套餐":
                    sql = sql + "and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";
                    break;
            }
        }
        sql=sql+"\n"+"group by es.id,day"+"\n"+"order by day";
        Query q=entityManager.createNativeQuery(sql);
        List list = q.getResultList();
        Long bbTime=Long.parseLong(bTime)*1000;
        Long ttTime=Long.parseLong(tTime)*1000;
        Long oneDay=1000*60*60*24L;
        ArrayList days=new ArrayList();
        ArrayList loseDays=new ArrayList();
        while (bbTime<=ttTime){
            Date d=new Date(bbTime);
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            days.add(df.format(d));
            bbTime+=oneDay;
        }
        for (Object day : days) {
            boolean find = false;
            for (Object aList : list) {
                Date d = new Date(Long.parseLong(aList.toString())*1000);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (day.equals(df.format(d))) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                loseDays.add(day);
            }
        }
        return loseDays;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<WorkLoseStudentAcoin> getWorkLoseStudentAcoinCnt(String data,DatatablesCriterias criterias){
        String [] cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String combo=cutData[2];
        String sql=String.format("select es.id as id,es.nickname as name,ifNUll(eru.nickname,'UNKNOWN') as ghs,count(eail.create_time) as day,sum(eail.acoin) as acoin from ebk_students es\n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=es.id\n" +
                "LEFT JOIN ebk_acoin_io_log eail on eail.sid=es.id\n" +
                "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                "where eail.create_time>=%s and eail.create_time<=%s\n" +
                "and eail.acoin_io_type=22 and esi.study_aim=1 and es.status=1",bTime,tTime);
        if(!combo.equals("不限")){
            switch (combo) {
                case "菲律宾套餐":
                    sql = sql + "\n" + "and es.lsns_per_day!=0 and es.days_per_week!=0";
                    break;
                case "欧美套餐":
                    sql = sql + "\n" + "and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";
                    break;
                case "自由课套餐":
                    sql = sql;
                    break;
            }
        }
        sql=sql+"\n"+"group by es.id";
        TableQuery query = new TableQuery(entityManager, WorkLoseStudentAcoin.class, criterias, sql);
        DataSet<WorkLoseStudentAcoin> actions=query.getResultDataSet();
        workLoseClassCnt=query.getTotalCount();
        return actions;
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
                    "LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "where allorder.pay_time >=%s and allorder.pay_time<=%s\n" +
                    "and esi.study_aim=1 and allorder.payed=1 and allorder.order_flag=2",bTime,tTime);

            workRenewWholeSql=sql;
        }else{
            sql = String.format("select es.id as id,es.nickname as name, ifNUll(eru.nickname,'UNKNOWN') as ghs\n" +
                    "from ebk_students es\n" +
                    "LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                    "group by eaod.order_id,eaod.begin_time\n" +
                    "union\n" +
                    "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                    "INNER JOIN ebk_acoin_split_order easo on eao.sid=easo.sid\n" +
                    "group by eao.id,easo.begin_time) allorder on allorder.sid=es.id\n"+
                    "LEFT JOIN ebk_student_info esi on es.id = esi.sid\n" +
                    "LEFT JOIN ebk_rbac_user eru on eru.id=es.ghs\n" +
                    "where allorder.pay_time >=%s and allorder.pay_time<=%s\n" +
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
                "LEFT JOIN (select eao.pay_time,eao.payed,eao.sid,eao.id,eaod.combo_name,eao.tmoney,eaod.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
                "INNER JOIN ebk_acoin_order_detail eaod on eao.id=eaod.order_id \n" +
                "group by eaod.order_id,eaod.begin_time\n" +
                "union\n" +
                "select eao.pay_time,eao.payed,eao.sid,eao.id,easo.combo_name,eao.tmoney,easo.tch_from,eao.order_flag,eao.upgrade_from as up from ebk_acoin_orders eao\n" +
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
    public LinkedHashMap<String,String> getDayStudentActivityChart(String data) throws ParseException {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String tData=data+" 23:59";
        data=data+" 00:00";
        String sql=String.format("select SUBSTR(wsal.time,1,16),COUNT(wsal.id) from ABC360_WEB_STUDENT_ACTION_LAST_MONTH_WITH_TODAY_TBL wsal \n" +
                "LEFT JOIN ebk_student_info esi on esi.sid=wsal.operatorid\n" +
                "where wsal.time between '%s' and '%s'\n" +
                "and esi.study_aim=1\n" +
                "group by SUBSTR(wsal.time,1,16);",data,tData);
        Query q = firstEntityManager.createNativeQuery(sql);
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = simpleDateFormat.parse(result[0].toString());
            String res = String.valueOf(date.getTime());
            map.put(res, result[1].toString());
        }
        return map;
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

    @Override
    @SuppressWarnings("unchecked")
    public String getWorkLoseClassSql(){return workLoseClassSql;}

    @Override
    @SuppressWarnings("unchecked")
    public Long getWorkLoseClassCnt(){return workLoseClassCnt;}
}
