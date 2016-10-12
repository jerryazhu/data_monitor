package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.web.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
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
        TableQuery query = new TableQuery(entityManager, WebStuAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebTeaAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebTeaActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebUserAction.class, criterias);
        return query.getResultDataSet();
    }


    @Override
    public DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebUserActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebErrorAction> findErrorActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebErrorAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebDebugAction> findDebugActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebDebugAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebCronAction> findCronActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebCronAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebRoleAction> findRoleActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebRoleAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<WebPropertyAction> findPropertyActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, WebPropertyAction.class, criterias);
        return query.getResultDataSet();
    }

}
