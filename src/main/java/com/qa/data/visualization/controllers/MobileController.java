package com.qa.data.visualization.controllers;


import com.qa.data.visualization.entities.mobile.*;
import com.qa.data.visualization.services.MobileActionService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping(value = "get_android_model")
    @ResponseBody
    public DatatablesResponse<AndroidModel> getAndroidModel(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<AndroidModel> actions = mobileActionService.getAndroidModel(criterias);
//        DataSet<AndroidModel> actions2=null;
//        DataRow dr=actions2.NewRow();
//        for(int i=0;i<actions.getRows().size();i++){
//            if(actions.getRows().get(i).getTime().length()==13){
//                actions2.getRows().add(actions.getRows().get(i));
//            }
//        }
        return DatatablesResponse.build(actions, criterias);
    }
//    @RequestMapping(value = "/get_android_model_cnt")
//    @ResponseBody
//    public


}
