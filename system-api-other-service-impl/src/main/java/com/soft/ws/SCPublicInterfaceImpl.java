package com.soft.ws;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.service.his.*;
import com.soft.api.service.lis.QueryPatientChaperoneImpl;
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
    private QueryPatientInfoImpl queryPatientInfo;
    
    @Autowired
    private QueryInhospitalInfoImpl queryInhospitalInfo;

    @Autowired
    private QueryPatientChaperoneImpl queryPatientChaperone;

    @Autowired
    private QueryPatientOrderInhospitalImpl queryPatientOrderInhospital;

    @Autowired
    private QueryFeeCatalogInfoImpl queryFeeCatalogInfo;

    @Autowired
    private InhospitalFillUpFeeImpl inhospitalFillUpFee;

    @Autowired
    private InhospitalCancelFillUpFeeImpl inhospitalCancelFillUpFee;
    /**
     * 查询业务系统患者信息
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryPatientInfo", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryPatientInfo(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryPatientInfo.queryPatientInfo(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryPatientInfo：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询业务系统患者信息【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Patient");
        }
        return JSONObject.toJSONString(responseEntity);
    }

    /**
     * 查询业务系统患者住院记录
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryInhospitalInfo", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
	@Override
	public String QueryInhospitalInfo(@WebParam(name="param") String param) {
    	 String type = "";
         param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
         ResponseEntity responseEntity = null;
         try {
             type = ObjectToMap.getObjectType(param);
             Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
             responseEntity = queryInhospitalInfo.queryInhospitalInfo(paramMap);
         }catch (Exception e) {
             e.printStackTrace();
             log.error("QueryPatientInfo：异常【"+e.getMessage()+"】");
             responseEntity = ResultMessage.error("查询业务系统患者信息【"+e.getMessage()+"】");
         }
         if(type.equals("XML")){
             return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Patient");
         }
         return JSONObject.toJSONString(responseEntity);
		
	}

    /**
     * 查询住院患者陪护者信息
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryPatientChaperone", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryPatientChaperone(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryPatientChaperone.queryPatientChaperone(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryPatientChaperone：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询住院患者陪护者信息【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Patient");
        }
        return JSONObject.toJSONString(responseEntity);

    }

    /**
     * 查询住院患者陪护者信息
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryPatientChaperoneNucleateResult", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryPatientChaperoneNucleateResult(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryPatientChaperone.queryPatientChaperoneNucleateResult(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryPatientChaperoneNucleateResult：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询住院患者陪护者核算检测结果【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Patient");
        }
        return JSONObject.toJSONString(responseEntity);

    }

    /**
     * 查询预约住院病人信息
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryPatientOrderInhospital", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryPatientOrderInhospital(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryPatientOrderInhospital.QueryPatientOrderInhospital(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryPatientOrderInhospital：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询预约住院病人信息【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Patient");
        }
        return JSONObject.toJSONString(responseEntity);

    }

    /**
     * 查询医疗收费目录信息
     * @param param
     * @return
     */
    @WebMethod(
            operationName="QueryFeeCatalogInfo", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryFeeCatalogInfo(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryFeeCatalogInfo.queryFeeCatalog(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryFeeCatalogInfo：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("查询医疗收费目录信息【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Catalog");
        }
        return JSONObject.toJSONString(responseEntity);
    }

    /**
     * 住院补计费用（zy_fymx）
     * @param param
     * @return
     */
    @WebMethod(
            operationName="InhospitalFillUpFee", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String InhospitalFillUpFee(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = inhospitalFillUpFee.inhospitalFillUpFee(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("InhospitalFillUpFee：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("住院补计费用【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Fee");
        }
        return JSONObject.toJSONString(responseEntity);
    }


    /**
     * 住院作废补计费用（zy_fymx）
     * @param param
     * @return
     */
    @WebMethod(
            operationName="InhospitalCancelFillUpFee", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String InhospitalCancelFillUpFee(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = inhospitalCancelFillUpFee.inhospitalCancelFillUpFee(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("InhospitalFillUpFee：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("住院作废补计费用【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity).replaceAll("QueryResult","Fee");
        }
        return JSONObject.toJSONString(responseEntity);
    }


}
