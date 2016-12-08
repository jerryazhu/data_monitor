package com.qa.data.visualization.controllers;

import com.qa.data.visualization.entities.jishu.pc.PCAPIStuActionGroupCount;
import com.qa.data.visualization.entities.jishu.pc.PCStuAPIAction;
import com.qa.data.visualization.entities.jishu.pc.PCTeaAPIAction;
import com.qa.data.visualization.services.jishu.PCService;
import com.web.spring.datatable.DataSet;
import com.web.spring.datatable.DatatablesCriterias;
import com.web.spring.datatable.DatatablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/pc")
public class PCController {

    @Autowired
    private PCService pcService;

    @RequestMapping(value = "/get_pc_stu_api_action")
    @ResponseBody
    public DatatablesResponse<PCStuAPIAction> findPCStuAPIActionsWithDatatablesCriterias(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<PCStuAPIAction> actions = pcService.findPCStuAPIActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_pc_stu_api_action_group")
    @ResponseBody
    public DatatablesResponse<PCAPIStuActionGroupCount> findPCAPIStuActionGroupCount(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<PCAPIStuActionGroupCount> actions = pcService.findPCAPIStuActionGroupCount(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value = "/get_pc_tea_api_action")
    @ResponseBody
    public DatatablesResponse<PCTeaAPIAction> findPCTeaAPIActionsWithDatatablesCriterias(HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<PCTeaAPIAction> actions = pcService.findPCTeaAPIActionsWithDatatablesCriterias(criterias);
        return DatatablesResponse.build(actions, criterias);
    }

    @RequestMapping(value="/get_pc_api_response/{data}")
    @ResponseBody
    public String getPcApiResponse(@PathVariable String data){
        HashMap <String,String> pcStudentApiResponse=pcService.getPcApiResponse(data);
        return pcStudentApiResponse.get(data);
    }

}
