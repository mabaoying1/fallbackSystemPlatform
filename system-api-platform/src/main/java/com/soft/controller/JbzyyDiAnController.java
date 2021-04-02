package com.soft.controller;

import com.soft.service.JbzyyDiAnService;

import ctd.net.rpc.util.ResultCode;
import ctd.net.rpc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 迪安一般检验结果描述
 * @ClassName: JbzyyDiAnController
 * @Description: TODO
 * @author: caidao
 * @date: 2020年4月7日 下午4:35:30
 */
@Controller
public class JbzyyDiAnController {
    @Autowired
    private JbzyyDiAnService jbzyyDiAnService;
    public String DiAnInspection(String url,String clientID, String clientGUID,String method) {
        // TODO Auto-generated method stub
        if(StringUtil.isEmpty(url)) {
            return "url不能为空";
        }
        if(StringUtil.isEmpty(clientID)) {
            return "clientID不能为空";
        }
        if(StringUtil.isEmpty(clientGUID)) {
            return "clientGUID不能为空";
        }
        if(StringUtil.isEmpty(method)) {
            return "method不能为空";
        }

        try {

            Map<String, Object> response = new LinkedHashMap<String, Object>();
            try {
                String resultMsg = jbzyyDiAnService.parseAndSaveDiAnResult(url,clientID,clientGUID,method);
                response.put("Status", true);
                response.put("Detail", "成功");
                response.put("msg", resultMsg);
            } catch (Exception e) {
                e.printStackTrace();
                response.put("Status", false);
                response.put("ErrCode", ResultCode.DATABASE_ERROR);
                response.put("Detail", e.getMessage());
            }

            return response.toString() ;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "DiAnInspection一般检验接口异常【"+e.getMessage()+"】";
        }
    }
}
