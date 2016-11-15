package com.qa.data.visualization.services;


import com.qa.data.visualization.entities.mobile.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.TableQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class MobileActionServiceImpl implements MobileActionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, AndroidStuAPIAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, IosStuAPIAction.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, AndroidAPIStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, IOSAPIStuActionGroupCount.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<AndroidModel> getAndroidModel(DatatablesCriterias criterias) {
        TableQuery query = new TableQuery(entityManager, AndroidModel.class, criterias);
        return query.getResultDataSet();
    }

    @Override
    public DataSet<AndroidModelCnt> getAndroidModelCnt(DatatablesCriterias criterias) {
        String customSQL = "select model as model,count(model) as count from ( select * from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000)) a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL where LENGTH(time)=13 and time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000) group by uid,model) b\n" +
                "on a.time = b.time and a.uid = b.uid\n" +
                "GROUP BY (model) order by count desc";
        TableQuery query = new TableQuery(entityManager, AndroidModelCnt.class, criterias, customSQL);
        return query.getResultDataSet();
    }
}
