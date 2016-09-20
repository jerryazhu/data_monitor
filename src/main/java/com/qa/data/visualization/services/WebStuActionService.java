package com.qa.data.visualization.services;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.LastWebStuAction;

import java.util.LinkedHashMap;

public interface WebStuActionService {
    LinkedHashMap<String, String> getHotSpot();

    DataSet<LastWebStuAction> findLastActionsWithDatatablesCriterias(DatatablesCriterias criterias);
}
