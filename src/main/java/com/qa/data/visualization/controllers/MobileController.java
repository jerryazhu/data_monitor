package com.qa.data.visualization.controllers;


import com.qa.data.visualization.entities.mobile.*;
import com.qa.data.visualization.services.MobileActionService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import com.web.spring.datatable.TableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/mobile")
public class MobileController {
    @PersistenceContext
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
        String customSQL = "select model as model,count(model) as count from ( select * from ABC360_ANDROID_APP_DEVICE_TBL where time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000)) a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL where LENGTH(time)=13 and time > (UNIX_TIMESTAMP(now())*1000 - 3600*24*30*1000) group by uid,model) b\n" +
                "on a.time = b.time and a.uid = b.uid\n" +
                "GROUP BY (model) order by count desc";
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        TableQuery query = new TableQuery(entityManager, AndroidModelCnt.class, criterias, customSQL);
        DataSet<AndroidModelCnt> dataSet = query.getResultDataSet();
        return DatatablesResponse.build(dataSet, criterias);
    }
}
