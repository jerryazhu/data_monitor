package com.qa.data.visualization.services;


import com.qa.data.visualization.entities.mobile.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
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
    public DataSet<AndroidModel> getAndroidModel(DatatablesCriterias criterias) {
        Query query = new Query(entityManager, AndroidModel.class, criterias);
        return query.getResultDataSet();
    }
}
