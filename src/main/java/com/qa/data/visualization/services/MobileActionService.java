package com.qa.data.visualization.services;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.mobile.AndroidAPIStuActionGroupCount;
import com.qa.data.visualization.entities.mobile.AndroidStuAPIAction;
import com.qa.data.visualization.entities.mobile.IOSAPIStuActionGroupCount;
import com.qa.data.visualization.entities.mobile.IosStuAPIAction;

public interface MobileActionService {
    DataSet<AndroidStuAPIAction> findAndroidStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<IosStuAPIAction> findIosStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(DatatablesCriterias criterias);
}
