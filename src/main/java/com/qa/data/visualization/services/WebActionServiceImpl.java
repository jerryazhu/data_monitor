package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.web.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class WebActionServiceImpl implements WebActionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, String> getHotSpot() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        javax.persistence.Query q = entityManager.createNativeQuery("select left(right(time,8),5) as min,count(*) as cnt,time  from ABC360_WEB_STUDENT_ACTION_LAST_TBL group by min ORDER BY time ASC");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[2].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    public DataSet<WebStuAction> findStuActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebStuAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebTeaAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebTeaActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebUserAction.class, criterias);
        return query.getResultDataSet();
    }


    @Override
    public DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebUserActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebErrorAction> findErrorActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebErrorAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebDebugAction> findDebugActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebDebugAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebCronAction> findCronActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebCronAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebRoleAction> findRoleActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebRoleAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebPropertyAction> findPropertyActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, WebPropertyAction.class, criterias);
        return query.getResultDataSet();
    }

}
