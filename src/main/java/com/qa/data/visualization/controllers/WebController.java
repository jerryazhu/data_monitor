package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.web.*;
import com.qa.data.visualization.services.WebActionService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/web")
public class WebController {

    @Autowired
    private WebActionService webActionService;

    @RequestMapping(value = "/get_web_stu_action")
    @ResponseBody
    public DatatablesResponse<WebStuAction> findAllStuForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuAction> actions = webActionService.findStuActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_stu_action_group")
    @ResponseBody
    public DatatablesResponse<WebStuActionGroupCount> findStuGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebStuActionGroupCount> actions = webActionService.findStuGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_tea_action")
    @ResponseBody
    public DatatablesResponse<WebTeaAction> findAllTeaForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebTeaAction> actions = webActionService.findTeaActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_tea_action_group")
    @ResponseBody
    public DatatablesResponse<WebTeaActionGroupCount> findTeaGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebTeaActionGroupCount> actions = webActionService.findTeaGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_user_action")
    @ResponseBody
    public DatatablesResponse<WebUserAction> findAllUserForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebUserAction> actions = webActionService.findUserActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_user_action_group")
    @ResponseBody
    public DatatablesResponse<WebUserActionGroupCount> findUserGroupCountForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebUserActionGroupCount> actions = webActionService.findUserGroupCountWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_error_action")
    @ResponseBody
    public DatatablesResponse<WebErrorAction> findAllErrorForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebErrorAction> actions = webActionService.findErrorActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_debug_action")
    @ResponseBody
    public DatatablesResponse<WebDebugAction> findAllDebugForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebDebugAction> actions = webActionService.findDebugActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_cron_action")
    @ResponseBody
    public DatatablesResponse<WebCronAction> findAllCronForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebCronAction> actions = webActionService.findCronActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_role_action")
    @ResponseBody
    public DatatablesResponse<WebRoleAction> findAllRoleForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebRoleAction> actions = webActionService.findRoleActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_web_property_action")
    @ResponseBody
    public DatatablesResponse<WebPropertyAction> findAllPropertyForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<WebPropertyAction> actions = webActionService.findPropertyActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }
}
