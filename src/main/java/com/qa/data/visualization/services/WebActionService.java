package com.qa.data.visualization.services;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.qa.data.visualization.entities.web.*;

import java.util.LinkedHashMap;

public interface WebActionService {
    LinkedHashMap<String, String> getHotSpot();

    DataSet<WebStuAction> findStuActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebErrorAction> findErrorActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebDebugAction> findDebugActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebCronAction> findCronActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebRoleAction> findRoleActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebPropertyAction> findPropertyActionsWithDatatablesCriterias(DatatablesCriterias criterias);
}
