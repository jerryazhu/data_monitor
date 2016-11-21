package com.qa.data.visualization.services.qingshao;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Query;

/**
 * Created by dykj on 2016/11/17.
 */
@Service
public class ClassServiceImpl implements ClassService{
    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList getTeacherGroup(){
        ArrayList getGroup=new ArrayList();
        Query q=entityManager.createNativeQuery("select distinct ebk_teacher_group.title\n" +
                "from ebk_teachers inner join ebk_teacher_group on ebk_teachers.workgroup = ebk_teacher_group.id\n" +
                "where ebk_teachers.tch_course = 1 order by title ");
        List list=q.getResultList();
        for (Object aList : list) {
            getGroup.add(aList.toString());
        }
        return getGroup;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String,String> getTeacherMessage(String data){
        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
        Query q=entityManager.createNativeQuery("select id,nickname from ebk_teachers where (id like '%"+data+"%' or nickname like '%"+data+"%') and nickname!=''");
        List<Object[]> list=q.getResultList();
        for(Object[] result:list){
            map.put(result[0].toString(),result[1].toString());
        }
        return map;
    }
}
