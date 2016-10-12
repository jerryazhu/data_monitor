package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.mobile.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.util.LinkedHashMap;
import java.util.List;

public interface MobileActionService {
    DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<AndroidModel> getAndroidModel(DatatablesCriterias criterias);
}
