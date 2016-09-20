package com.qa.data.visualization.controllers;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.qa.data.visualization.entities.LastWebStuAction;
import com.qa.data.visualization.services.WebStuActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebStuActionController {

    @Autowired
    private WebStuActionService webStuActionService;

    @RequestMapping(value = "/get_web_stu_action")
    @ResponseBody
    public DatatablesResponse<LastWebStuAction> findAllForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<LastWebStuAction> actions = webStuActionService.findLastActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

}
