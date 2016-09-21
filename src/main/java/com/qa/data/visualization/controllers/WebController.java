package com.qa.data.visualization.controllers;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.qa.data.visualization.entities.WebStuAction;
import com.qa.data.visualization.entities.WebStuActionGroupCount;
import com.qa.data.visualization.services.WebStuActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/web")
public class WebController {

    @Autowired
    private WebStuActionService webStuActionService;

    @RequestMapping(value = "/get_web_stu_action")
    @ResponseBody
    public DatatablesResponse<WebStuAction> findAllForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuAction> actions = webStuActionService.findActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_stu_action_group")
    @ResponseBody
    public DatatablesResponse<WebStuActionGroupCount> findGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuActionGroupCount> actions = webStuActionService.findGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

}
