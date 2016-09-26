package com.qa.data.visualization.services;


import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.pc.PCAPIStuActionGroupCount;
import com.qa.data.visualization.entities.pc.PCStuAPIAction;
import com.qa.data.visualization.entities.pc.PCTeaAPIAction;
import com.qa.data.visualization.util.QueryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PCActionServiceImpl implements PCActionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DataSet<PCStuAPIAction> findPCStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, PCStuAPIAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<PCAPIStuActionGroupCount> findPCAPIStuActionGroupCount(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, PCAPIStuActionGroupCount.class, criterias);
        return queryUtils.getResultDataSet();
    }

    @Override
    public DataSet<PCTeaAPIAction> findPCTeaAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        QueryUtils queryUtils = new QueryUtils(entityManager, PCTeaAPIAction.class, criterias);
        return queryUtils.getResultDataSet();
    }

}
