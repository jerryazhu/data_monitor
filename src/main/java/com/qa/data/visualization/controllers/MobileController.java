package com.qa.data.visualization.controllers;


import com.qa.data.visualization.entities.jishu.mobile.*;
import com.qa.data.visualization.services.jishu.MobileActionService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/mobile")
public class MobileController {
    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private EntityManager entityManager;
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

    @RequestMapping(value = "/get_android_response/{data}")
    @ResponseBody
    public String getAndroidResponse(@PathVariable String data) {
        HashMap<String, String> androidResponse = mobileActionService.getAndroidResponse(data);
        return androidResponse.get(data);
    }

    @RequestMapping(value = "/get_ios_response/{data}")
    @ResponseBody
    public String getIosResponse(@PathVariable String data) {
        HashMap<String, String> iosResponse = mobileActionService.getIosResponse(data);
        return iosResponse.get(data);
    }

    @RequestMapping(value = "/get_ios_api_stu_action_group")
    @ResponseBody
    public DatatablesResponse<IOSAPIStuActionGroupCount> findIOSAPIStuActionGroupCount(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<IOSAPIStuActionGroupCount> actions = mobileActionService.findIOSAPIStuActionGroupCount(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_android_model")
    @ResponseBody
    public DatatablesResponse<AndroidModel> getAndroidModel(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidModel> dataSet = mobileActionService.getAndroidModel(criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/get_android_model_cnt")
    @ResponseBody
    public DatatablesResponse<AndroidModelCnt> getAndroidModelCnt(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidModelCnt> dataSet = mobileActionService.getAndroidModelCnt(criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/get_android_crash")
    @ResponseBody
    public DatatablesResponse<AndroidCrash> getAndroidCrash(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidCrash> dataSet = mobileActionService.getAndroidCrash(criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }
}
