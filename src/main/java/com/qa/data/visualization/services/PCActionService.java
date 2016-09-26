package com.qa.data.visualization.services;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.pc.PCAPIStuActionGroupCount;
import com.qa.data.visualization.entities.pc.PCStuAPIAction;
import com.qa.data.visualization.entities.pc.PCTeaAPIAction;

public interface PCActionService {
    DataSet<PCStuAPIAction> findPCStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<PCAPIStuActionGroupCount> findPCAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<PCTeaAPIAction> findPCTeaAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);
}
