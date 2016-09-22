package com.qa.data.visualization.services;


import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.*;
import com.qa.data.visualization.util.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        Query q = entityManager.createNativeQuery("select left(right(time,8),5) as min,count(*) as cnt,time  from ABC360_WEB_STUDENT_ACTION_LAST_TBL group by min ORDER BY time ASC");
        List<Object[]> list = q.getResultList();
        for (Object[] result : list) {
            map.put(result[2].toString(), result[1].toString());
        }
        return map;
    }

    @Override
    public DataSet<WebStuAction> findStuActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebStuAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebStuActionGroupCount.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebTeaAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebTeaActionGroupCount.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebUserAction.class, criterias);
        return queryUtils.getResultDataSet();
    }


    @Override
    public DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebUserActionGroupCount.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebErrorAction> findErrorActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebErrorAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebDebugAction> findDebugActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebDebugAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebCronAction> findCronActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebCronAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebRoleAction> findRoleActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebRoleAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<WebPropertyAction> findPropertyActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebPropertyAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

}
