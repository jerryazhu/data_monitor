package com.qa.data.visualization.services;


import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.WebStuAction;
import com.qa.data.visualization.entities.WebStuActionGroupCount;
import com.qa.data.visualization.util.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class WebStuActionServiceImpl implements WebStuActionService {
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

    public DataSet<WebStuAction> findActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager);
        List<WebStuAction> actions = queryUtils.getRecordsWithDatatablesCriterias(WebStuAction.class, criterias);
        Long count = queryUtils.getTotalCount(WebStuAction.class);
        Long countFiltered = queryUtils.getFilteredCount(WebStuAction.class, criterias);
        return new DataSet<WebStuAction>(actions, count, countFiltered);
    }

    public DataSet<WebStuActionGroupCount> findGroupCountWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager);
        List<WebStuActionGroupCount> actions = queryUtils.getRecordsWithDatatablesCriterias(WebStuActionGroupCount.class, criterias);
        Long count = queryUtils.getTotalCount(WebStuActionGroupCount.class);
        Long countFiltered = queryUtils.getFilteredCount(WebStuActionGroupCount.class, criterias);
        return new DataSet<WebStuActionGroupCount>(actions, count, countFiltered);
    }

}
