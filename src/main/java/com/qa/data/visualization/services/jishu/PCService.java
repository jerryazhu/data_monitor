package com.qa.data.visualization.services.jishu;

import com.qa.data.visualization.entities.jishu.pc.PCAPIStuActionGroupCount;
import com.qa.data.visualization.entities.jishu.pc.PCStuAPIAction;
import com.qa.data.visualization.entities.jishu.pc.PCTeaAPIAction;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.util.LinkedHashMap;

public interface PCService {
    DataSet<PCStuAPIAction> findPCStuAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<PCAPIStuActionGroupCount> findPCAPIStuActionGroupCount(DatatablesCriterias criterias);

    DataSet<PCTeaAPIAction> findPCTeaAPIActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    LinkedHashMap<String, String> getPCApp();

    LinkedHashMap<String, String> getPCSystem();
}
