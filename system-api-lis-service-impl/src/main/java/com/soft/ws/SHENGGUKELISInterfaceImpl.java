package com.soft.ws;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.service.lis.shengguke.*;
import com.soft.api.util.FreemarkerUtilNew;
import ctd.net.rpc.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.Map;

/**
 * 省骨科LIS接口 <br>
 * 第三方检验流程描述：瑞美：申请单接收--条码打印--采集--出报告<br>
 * @ClassName SHENGGUKELISInterfaceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/2
 * @Version V1.0
 **/

@Slf4j
public class SHENGGUKELISInterfaceImpl implements ISHENGGUKELISInterface{

    @Autowired
    private LabBarcodeCreateImpl labBarcodeCreate;

    @Autowired
    private FreemarkerUtilNew freemarkerUtilNew;

    @Autowired
    private LabBarcodeCreateCancelImpl labBarcodeCreateCancel;

    @Autowired
    private LabSampleCollectORCancelImpl labSampleCollectORCancel;

    @Autowired
    private LabSampleSendORCancelImpl labSampleSendORCancel;

    @Autowired
    private LabSampleReceiveORReturnImpl labSampleReceiveORReturn;

    @Autowired
    private LabReportAuditORCancelImpl labReportAuditORCancel;

    @Autowired
    private LabRequestInHospitalPayBillImpl labRequestInHospitalPayBill;

    @Autowired
    private LabRequestInHospitalReturnBillImpl labRequestInHospitalReturnBill;

    @WebMethod(
            operationName="LabRequestInHospitalPayBill", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabRequestInHospitalPayBill(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labRequestInHospitalPayBill.labRequestInHospitalPayBill(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabRequestInHospitalPayBill：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验住院计费失败【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabRequestInHospitalReturnBill", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabRequestInHospitalReturnBill(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labRequestInHospitalReturnBill.labRequestInHospitalReturnBill(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabRequestInHospitalReturnBill：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验住院取消计费失败【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabBarcodeCreate", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabBarcodeCreate(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labBarcodeCreate.labBarcodeCreate(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabBarcodeCreate：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验条码打印通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
          return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabBarcodeCreateCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabBarcodeCreateCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labBarcodeCreateCancel.labBarcodeCreateCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabBarcodeCreateCancel：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("取消检验条码打印通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleCollect", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleCollect(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleCollectORCancel.labSampleCollect(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleCollect：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验标本采集通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleCollectCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleCollectCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleCollectORCancel.labSampleCollectCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleCollectCancel：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("取消检验标本采集通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleSend", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleSend(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleSendORCancel.labSampleSend(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleSend：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验标本送检通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleSendCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleSendCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleSendORCancel.labSampleSendCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleSendCancel：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("取消检验标本送检通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleReceive", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleReceive(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleReceiveORReturn.labSampleReceive(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleReceive：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验标本核收通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabSampleReturn", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabSampleReturn(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labSampleReceiveORReturn.labSampleReturn(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabSampleReturn：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验标本退回通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabReportAudit", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabReportAudit(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labReportAuditORCancel.labReportAudit(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabReportAudit：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验报告审核通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="LabReportAuditCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String LabReportAuditCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = labReportAuditORCancel.labReportAuditCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("LabReportAuditCancel：异常【"+e.getMessage()+"】");
            responseEntity = ResultMessage.error("检验报告取消审核通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return freemarkerUtilNew.freeMarker("responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }
}
