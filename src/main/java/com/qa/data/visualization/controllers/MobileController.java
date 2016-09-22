package com.qa.data.visualization.controllers;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.qa.data.visualization.entities.AndroidAPIStuActionGroupCount;
import com.qa.data.visualization.entities.AndroidStuAPIAction;
import com.qa.data.visualization.entities.IOSAPIStuActionGroupCount;
import com.qa.data.visualization.entities.IosStuAPIAction;
import com.qa.data.visualization.services.MobileActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/mobile")
public class MobileController {

    @Autowired
    private MobileActionService mobileActionService;

    @RequestMapping(value = "/get_android_stu_api_action")
    @ResponseBody
    public DatatablesResponse<AndroidStuAPIAction> findAndroidStuAPIForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidStuAPIAction> actions = mobileActionService.findAndroidStuAPIActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_ios_stu_api_action")
    @ResponseBody
    public DatatablesResponse<IosStuAPIAction> findIosStuAPIForDataTables(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<IosStuAPIAction> actions = mobileActionService.findIosStuAPIActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_android_api_stu_action_group")
    @ResponseBody
    public DatatablesResponse<AndroidAPIStuActionGroupCount> findAndroidAPIStuActionGroupCount(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidAPIStuActionGroupCount> actions = mobileActionService.findAndroidAPIStuActionGroupCount(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_ios_api_stu_action_group")
    @ResponseBody
    public DatatablesResponse<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<IOSAPIStuActionGroupCount> actions = mobileActionService.findIOSAPIStuActionGroupCount(criterias);
        return DatatablesResponse.build(actions, criterias);
    }


}
