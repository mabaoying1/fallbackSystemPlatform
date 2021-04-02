package com.soft.ws;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.service.his.SC.QueryExmRequestImpl;
import ctd.net.rpc.util.FreemarkerUtil;
import ctd.net.rpc.util.ObjectToMap;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.Map;

/**
 * @ClassName SCPublicInterfaceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/2/22
 * @Version V1.0
 **/
@Slf4j
public class SCPublicInterfaceImpl implements ISCPublicInterface{

    @Autowired
    private QueryExmRequestImpl queryExmRequest;

    /**
     * 查询申请单检查状态
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryExmRequest", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryExmRequest(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryExmRequest.queryExmRequest(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("queryExmRequest：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询检查申请单检查状态【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","ExmRequest");
        }
        return JSONObject.toJSONString(responseEntity);
    }
}
