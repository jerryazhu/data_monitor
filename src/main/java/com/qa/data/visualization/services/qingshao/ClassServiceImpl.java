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
    private Long costClassCnt;
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
        String sql="select ecr.begin_time as begin_time,ecr.tid as tid,ecr.tname as tname,ecr.sid as sid,ecr.sname as sname  \n" +
                "from ebk_class_records ecr\n" +
                "LEFT JOIN ebk_students es on ecr.sid = es.id\n" +
                "LEFT JOIN ebk_student_info esi on ecr.sid = esi.sid\n" +
                "LEFT JOIN ebk_teachers et on ecr.tid = et.id\n" +
                "LEFT JOIN ebk_teacher_group etg on et.workgroup = etg.id\n" +
                "where \n" +
                "ecr.status=3\n" +
                "and esi.study_aim=1 and ecr.free_try=0" ;
        String []cutData=data.split("\\+");
        String bTime=cutData[0];
        String tTime=cutData[1];
        String studentMessage=cutData[2];
        String comboCountry=cutData[3];
        String combo=cutData[4];
        String binding=cutData[5];
        String teacherMessage=cutData[6];
        String teacherStatus=cutData[7];
        String group=cutData[8];
        String teacherType=cutData[9];
        if(bTime.equals("all")||tTime.equals("all")){
            sql=sql+"\n"+"and ecr.begin_time>"+yesterdayUnix;
        }else{
            sql=sql+"\n"+"and ecr.begin_time between "+bTime+" and "+tTime;
        }
        if(!studentMessage.equals("all")){
            sql=sql+"\n"+"and es.id= "+studentMessage;
        }
        if(comboCountry.equals("不限")){
            switch (combo){
                case "不限":break;
                case "51":sql=sql+"\n"+"and ((es.lsns_per_day=1 and es.days_per_week=5) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=5))";break;
                case "52":sql=sql+"\n"+"and ((es.lsns_per_day=2 and es.days_per_week=5) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=5))";break;
                case "31":sql=sql+"\n"+"and ((es.lsns_per_day=1 and es.days_per_week=3) or (es.lsns_per_day_eu=1 and es.days_per_week_eu=3))";break;
                case "32":sql=sql+"\n"+"and ((es.lsns_per_day=2 and es.days_per_week=3) or (es.lsns_per_day_eu=2 and es.days_per_week_eu=3))";break;
                case "00":sql=sql+"\n"+"and (es.lsns_per_day=0 and es.days_per_week=0 and es.lsns_per_day_eu=0 and es.days_per_week_eu=0 and es.acoin!=0)";break;
            }
        }else{
            if(comboCountry.equals("菲律宾")){
                switch (combo){
                    case "不限":sql=sql+"\n"+"and es.lsns_per_day!=0 and es.days_per_week!=0";break;
                    case "51":sql=sql+"\n"+"and (es.lsns_per_day=1 and es.days_per_week=5)";break;
                    case "52":sql=sql+"\n"+"and (es.lsns_per_day=2 and es.days_per_week=5)";break;
                }
            }else{
                switch (combo){
                    case "不限":sql=sql+"\n"+"and es.lsns_per_day_eu!=0 and es.days_per_week_eu!=0";break;
                    case "51":sql=sql+"\n"+"and (es.lsns_per_day_eu=1 and es.days_per_week_eu=5)";break;
                    case "52":sql=sql+"\n"+"and (es.lsns_per_day_eu=2 and es.days_per_week_eu=5)";break;
                    case "31":sql=sql+"\n"+"and (es.lsns_per_day_eu=1 and es.days_per_week_eu=3)";break;
                    case "32":sql=sql+"\n"+"and (es.lsns_per_day_eu=2 and es.days_per_week_eu=3)";break;
                }
            }
        }
        if(!binding.equals("不限")){
            switch (binding){
                case "绑定":sql=sql+"\n"+"and ecr.is_bind=1";break;
                case "不绑定":sql=sql+"\n"+"and ecr.is_bind=-1";break;
            }
        }
        if(!teacherMessage.equals("all")){
            sql=sql+"\n"+"and et.id="+teacherMessage;
        }
        if(!group.equals("Group")){
            sql=sql+"\n"+"and etg.title='"+group+"'";
        }
        if(teacherStatus.equals("不限")){
            sql=sql+"\n"+"and (et.status=3 or et.status=4)";
        }else{
            switch (teacherStatus){
                case "试用":sql=sql+"\n"+"and et.status=3";break;
                case "活跃":sql=sql+"\n"+"and et.status=4";break;
            }
        }
        if(!teacherType.equals("不限")){
            switch (teacherType){
                case "菲律宾":sql=sql+"\n"+"and et.catalog=1";break;
                case "欧美":sql=sql+"\n"+"and et.catalog=2";break;
            }
        }
        TableQuery query = new TableQuery(entityManager, CostClass.class, criterias, sql);
        DataSet<CostClass> result=query.getResultDataSet();
        costClassCnt=query.getTotalCount();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Long getCostClassCnt(){
        return costClassCnt;
    }
}
