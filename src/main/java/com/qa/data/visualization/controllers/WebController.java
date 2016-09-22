package com.qa.data.visualization.controllers;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.qa.data.visualization.entities.*;
import com.qa.data.visualization.services.WebActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/web")
public class WebController {

    @Autowired
    private WebActionService webStuActionService;

    @RequestMapping(value = "/get_web_stu_action")
    @ResponseBody
    public DatatablesResponse<WebStuAction> findAllStuForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuAction> actions = webStuActionService.findStuActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_stu_action_group")
    @ResponseBody
    public DatatablesResponse<WebStuActionGroupCount> findStuGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuActionGroupCount> actions = webStuActionService.findStuGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_tea_action")
    @ResponseBody
    public DatatablesResponse<WebTeaAction> findAllTeaForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebTeaAction> actions = webStuActionService.findTeaActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_tea_action_group")
    @ResponseBody
    public DatatablesResponse<WebTeaActionGroupCount> findTeaGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebTeaActionGroupCount> actions = webStuActionService.findTeaGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_user_action")
    @ResponseBody
    public DatatablesResponse<WebUserAction> findAllUserForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebUserAction> actions = webStuActionService.findUserActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_user_action_group")
    @ResponseBody
    public DatatablesResponse<WebUserActionGroupCount> findUserGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebUserActionGroupCount> actions = webStuActionService.findUserGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_error_action")
    @ResponseBody
    public DatatablesResponse<WebErrorAction> findAllErrorForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebErrorAction> actions = webStuActionService.findErrorActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_debug_action")
    @ResponseBody
    public DatatablesResponse<WebDebugAction> findAllDebugForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebDebugAction> actions = webStuActionService.findDebugActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }
}
