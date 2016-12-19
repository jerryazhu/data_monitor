package com.qa.data.visualization.services.jishu;


import com.qa.data.visualization.entities.jishu.web.*;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;

import java.util.LinkedHashMap;

public interface WebActionService {
    LinkedHashMap<String, String> getHotSpot();

    DataSet<WebStuAction> findStuActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebStuActionGroupCount> findStuGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebTeaAction> findTeaActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebTeaActionGroupCount> findTeaGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebUserAction> findUserActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebUserActionGroupCount> findUserGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebCrmAction> findCrmActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebCrmActionGroupCount> findCrmGroupCountWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebErrorAction> findErrorActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebDebugAction> findDebugActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebCronAction> findCronActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebRoleAction> findRoleActionsWithDatatablesCriterias(DatatablesCriterias criterias);

    DataSet<WebPropertyAction> findPropertyActionsWithDatatablesCriterias(DatatablesCriterias criterias);
}
