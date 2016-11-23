package com.qa.data.visualization.services.qingshao;

import com.qa.data.visualization.entities.qingshao.AutoComplete;
import com.qa.data.visualization.entities.qingshao.CostClass;
import com.qa.data.visualization.entities.qingshao.EbkStudent;
import com.qa.data.visualization.entities.qingshao.EbkTeacher;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dykj on 2016/11/17.
 */
@Service
public class ClassServiceImpl implements ClassService {
    public static Long costClassCnt;
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
                "where ebk_teachers.tch_course = 1 order by title ");
        List list = q.getResultList();
        for (Object aList : list) {
            getGroup.add(aList.toString());
        }
        return getGroup;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataSet<CostClass> getCostClass(String data,DatatablesCriterias criterias) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String today = dateFormat.format( now );
        Date date=dateFormat .parse(today);
        long todayUnix=date.getTime();
        long yesterdayUnix=todayUnix/1000-86400;
        String []cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String timeSql;
        String studentMessage=cutData[2];
        String studentMessageSql;
        String comboCountry=cutData[3];
        String combo=cutData[4];
        String comboSql = null;
        String binding=cutData[5];
        String bindingSql = null;
        String teacherMessage=cutData[6];
        String teacherMessageSql;
        String teacherStatus=cutData[7];
        String teacherStatusSql=null;
        String group=cutData[8];
        String groupSql;
        String teacherType=cutData[9];
        String teacherTypeSql=null;
        if(bTime.equals("all")||tTime.equals("all")){
            timeSql="begin_time>"+yesterdayUnix;
        }else{
            timeSql="begin_time between "+bTime+" and "+tTime;
        }
        if(studentMessage.equals("all")){
            studentMessageSql="id != ''";
        }else{
            String[]student=studentMessage.split("   ");
            studentMessageSql="id = "+student[0];
        }
        System.out.println("取值为"+comboCountry+combo);
        if(comboCountry.equals("不限")){
            switch (combo){
                case "不限":comboSql="((lsns_per_day!=0 and days_per_week!=0) or (lsns_per_day_eu!=0 and days_per_week_eu!=0) or free_course!=0)";break;
                case "51":comboSql="((lsns_per_day=1 and days_per_week=5) or (lsns_per_day_eu=1 and days_per_week_eu=5))";break;
                case "52":comboSql="((lsns_per_day=2 and days_per_week=5) or (lsns_per_day_eu=2 and days_per_week_eu=5))";break;
                case "31":comboSql="((lsns_per_day=1 and days_per_week=3) or (lsns_per_day_eu=1 and days_per_week_eu=3))";break;
                case "32":comboSql="((lsns_per_day=2 and days_per_week=3) or (lsns_per_day_eu=2 and days_per_week_eu=3))";break;
                case "00":comboSql="(free_course!=0)";break;
            }
        }else{
            if(comboCountry.equals("菲律宾")){
                switch (combo){
                    case "不限":comboSql="((lsns_per_day!=0 and days_per_week!=0) or free_course!=0)";break;
                    case "51":comboSql="(lsns_per_day=1 and days_per_week=5)";break;
                    case "52":comboSql="(lsns_per_day=2 and days_per_week=5)";break;
                    case "31":comboSql="(lsns_per_day=1 and days_per_week=3)";break;
                    case "32":comboSql="(lsns_per_day=2 and days_per_week=3)";break;
                    case "00":comboSql="(free_course>free_course_2)";break;
                }
            }else{
                switch (combo){
                    case "不限":comboSql="((lsns_per_day_eu!=0 and days_per_week_eu!=0) or free_course!=0)";break;
                    case "51":comboSql="(lsns_per_day_eu=1 and days_per_week_eu=5)";break;
                    case "52":comboSql="(lsns_per_day_eu=2 and days_per_week_eu=5)";break;
                    case "31":comboSql="(lsns_per_day_eu=1 and days_per_week_eu=3)";break;
                    case "32":comboSql="(lsns_per_day_eu=2 and days_per_week_eu=3)";break;
                    case "00":comboSql="(free_course_2!=0)";break;
                }
            }
        }
        if(binding.equals("不限")){
            bindingSql="is_bind!=0";
        }
        else{
            switch (binding){
                case "绑定":bindingSql="is_bind=1";
                case "不绑定":bindingSql="is_bind=-1";
            }
        }
        if(teacherMessage.equals("all")){
            teacherMessageSql="id!=''";
        }else{
            String []teacher=teacherMessage.split("   ");
            teacherMessageSql="id="+teacher[0];
        }
        if(group.equals("Group")){
            groupSql="title!=''";
        }else{
            groupSql="title='"+group+"'";
        }
        if(teacherStatus.equals("不限")){
            teacherStatusSql="(status=3 or status=4)";
        }else{
            switch (teacherStatus){
                case "试用":teacherStatusSql="status=3";break;
                case "活跃":teacherStatusSql="status=4";break;
            }
        }
        if(teacherType.equals("不限")){
            teacherTypeSql="catalog!=0";
        }else{
            switch (teacherType){
                case "菲律宾":teacherTypeSql="catalog=1";break;
                case "欧美":teacherTypeSql="catalog=2";break;
            }
        }
        String sql="select begin_time,tid,tname,sid,sname from ebk_class_records where "+timeSql+" and status=3 \n" +
                "and sid in(select id from ebk_students where "+studentMessageSql+"\n" +
                "and id in(select sid from ebk_student_info where study_aim=1) \n"+
                "and "+comboSql+")\n" +
                "and "+bindingSql+"\n" +
                "and tid in (select id from ebk_teachers where "+teacherMessageSql+"\n" +
                "and "+teacherStatusSql+" and "+teacherTypeSql+"\n" +
                "and workgroup in (select id from ebk_teacher_group where "+groupSql+"))";
        System.out.println(sql);
        TableQuery query = new TableQuery(entityManager, CostClass.class, criterias, sql);
        costClassCnt=query.getTotalCount();
        return query.getResultDataSet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getCostClassCnt(){
        return costClassCnt;
    }
}
