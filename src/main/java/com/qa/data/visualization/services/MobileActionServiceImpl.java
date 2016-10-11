package com.qa.data.visualization.services;


import com.qa.data.visualization.entities.mobile.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MobileActionServiceImpl implements MobileActionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, AndroidStuAPIAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, IosStuAPIAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, AndroidAPIStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, IOSAPIStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }
    @Override
    @SuppressWarnings("unchecked")
    public List getAndroidModel() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        javax.persistence.Query q = entityManager.createNativeQuery("select time,SUBSTRING(time FROM 0 FOR 10) from ABC360_ANDROID_APP_DEVICE_TBL where LENGTH(time)=13 and SUBSTRING(time FROM 0 FOR 10)<UNIX_TIMESTAMP(NOW()) order by time desc ");
        return q.getResultList();
    }
}
