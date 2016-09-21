package com.qa.data.visualization.services;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.WebStuAction;
import com.qa.data.visualization.entities.WebStuActionGroupCount;

import java.util.LinkedHashMap;

public interface WebStuActionService {
    LinkedHashMap<String, String> getHotSpot();

    DataSet<WebStuAction> findActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebStuActionGroupCount> findGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);
}
