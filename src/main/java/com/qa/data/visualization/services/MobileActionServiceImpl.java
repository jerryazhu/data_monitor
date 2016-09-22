package com.qa.data.visualization.services;


import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.AndroidAPIStuActionGroupCount;
import com.qa.data.visualization.entities.AndroidStuAPIAction;
import com.qa.data.visualization.entities.IOSAPIStuActionGroupCount;
import com.qa.data.visualization.entities.IosStuAPIAction;
import com.qa.data.visualization.util.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class MobileActionServiceImpl implements MobileActionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, AndroidStuAPIAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, IosStuAPIAction.class, criterias);
        ;
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, AndroidAPIStuActionGroupCount.class, criterias);
        ;
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, IOSAPIStuActionGroupCount.class, criterias);
        ;
        return queryUtils.getResultDataSet();
    }
}
