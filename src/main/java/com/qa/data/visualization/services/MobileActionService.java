package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.mobile.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.util.HashMap;

public interface MobileActionService {
    DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    HashMap<String,String> getAndroidResponse(String data);

    HashMap<String,String> getIosResponse(String data);

    DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<AndroidModel> getAndroidModel(DatatablesCriterias criterias);

    DataSet<AndroidModelCnt> getAndroidModelCnt(DatatablesCriterias criterias);
}
