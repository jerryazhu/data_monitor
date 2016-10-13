package com.qa.data.visualization.controllers;


import com.qa.data.visualization.auth.entities.User;
import com.qa.data.visualization.entities.mobile.*;
import com.qa.data.visualization.services.MobileActionService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public DatatablesResponse<AndroidModelCnt> getAndroidModelCnt(HttpServletRequest request){
        String queryBuilder="select model as model,count(model) as count from ABC360_ANDROID_APP_DEVICE_TBL a inner JOIN\n" +
                "(select uid,max(time) as time from ABC360_ANDROID_APP_DEVICE_TBL  where LENGTH(time)=13 and time<UNIX_TIMESTAMP(now())*1000 group by uid) b\n" +
                "on a.time = b.time and a.uid = b.uid \n" +
                "GROUP BY (model) order by count desc";
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        Query query = this.entityManager.createNativeQuery(queryBuilder);
        Long max=Long.valueOf(query.getResultList().size());
        query.setFirstResult(criterias.getStart());
        if(criterias.getLength()==-1){
            query.setMaxResults(query.getResultList().size());
        }else{
            query.setMaxResults(criterias.getLength());
        }
        List<Object[]> result = query.getResultList();
        List<AndroidModelCnt> result1=new ArrayList<AndroidModelCnt>();
        for(int i=0;i<result.size();i++){
            Object[] objects=result.get(i);
            AndroidModelCnt amc=new AndroidModelCnt();
            amc.setModel(objects[0].toString());
            amc.setCount(objects[1].toString());
            result1.add(amc);
        }
        DataSet<AndroidModelCnt> dataSet=new DataSet<AndroidModelCnt>(result1,max, max);
        return DatatablesResponse.build(dataSet,criterias);
    }
}
