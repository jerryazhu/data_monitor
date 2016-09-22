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
        List<WebStuAction> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebStuAction>(actions, count, countFiltered);
    }

    @Override
    public DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebStuActionGroupCount.class, criterias);
        List<WebStuActionGroupCount> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebStuActionGroupCount>(actions, count, countFiltered);
    }

    @Override
    public DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebTeaAction.class, criterias);
        List<WebTeaAction> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebTeaAction>(actions, count, countFiltered);
    }

    @Override
    public DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebTeaActionGroupCount.class, criterias);
        List<WebTeaActionGroupCount> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebTeaActionGroupCount>(actions, count, countFiltered);
    }

    @Override
    public DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebUserAction.class, criterias);
        List<WebUserAction> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebUserAction>(actions, count, countFiltered);
    }


    @Override
    public DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, WebUserActionGroupCount.class, criterias);
        List<WebUserActionGroupCount> actions = queryUtils.getRecordsWithDatatablesCriterias();
        Long count = queryUtils.getTotalCount();
        Long countFiltered = queryUtils.getFilteredCount();
        return new DataSet<WebUserActionGroupCount>(actions, count, countFiltered);
    }

}
