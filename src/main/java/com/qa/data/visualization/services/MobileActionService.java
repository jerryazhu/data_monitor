package com.qa.data.visualization.services;

import com.qa.data.visualization.entities.mobile.AndroidAPIStuActionGroupCount;
import com.qa.data.visualization.entities.mobile.AndroidStuAPIAction;
import com.qa.data.visualization.entities.mobile.IOSAPIStuActionGroupCount;
import com.qa.data.visualization.entities.mobile.IosStuAPIAction;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

public interface MobileActionService {
    DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias);
}
