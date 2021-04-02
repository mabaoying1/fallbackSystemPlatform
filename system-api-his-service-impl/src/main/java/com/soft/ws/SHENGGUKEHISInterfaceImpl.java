package com.soft.ws;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.service.his.shengguke.*;
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
 * @ClassName SHENGGUKEHISInterface
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
@Slf4j
public class SHENGGUKEHISInterfaceImpl implements ISHENGGUKEHISInterface{

    @Autowired
    private EMRDocumentRegistryImpl emrDocumentRegistry;

    @Autowired
    private ExmReportAuditORCancelImpl exmReportAuditORCancel;

    @Autowired
    private ExmRequestExecutedImpl exmRequestExecuted;

    @Autowired
    private ExmRequestExecutedCancelImpl exmRequestExecutedCancel;

    @Autowired
    private OperationImpl operation;

    @Autowired
    private PatientRegistryImpl patientRegistry;

    @Autowired
    private PatientUpdateImpl patientUpdate;

    @Autowired
    private TransfusionDoctorAdviceImpl transfusionDoctorAdvice;

    @Autowired
    private TransfusionDoctorAdviceCancelImpl transfusionDoctorAdviceCancel;

    @Autowired
    private TransfusionPayBillImpl transfusionPayBill;

    @Autowired
    private TransfusionReturnBillImpl transfusionReturnBill;

    @Autowired
    private QueryMedicalProjectImpl queryMedicalProject;

    @Autowired
    private QuerySfxmImpl querySfxm;

    @WebMethod(
            operationName="QuerySfxm", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QuerySfxm(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = querySfxm.querySfxm(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QuerySfxm：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("查询收费项目失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="QueryMedicalProject", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String QueryMedicalProject(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = queryMedicalProject.queryMedicalProject(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("QueryMedicalProject：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("查询医疗目录失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseQueryBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="TransfusionReturnBill", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String TransfusionReturnBill(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = transfusionReturnBill.transfusionReturnBill(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("TransfusionReturnBill：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("输血退费失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="TransfusionPayBill", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String TransfusionPayBill(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = transfusionPayBill.transfusionPayBill(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("TransfusionPayBill：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("输血计费失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="TransfusionDoctorAdviceCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String TransfusionDoctorAdviceCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = transfusionDoctorAdviceCancel.transfusionDoctorAdviceCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("TransfusionDoctorAdvice：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("输血医嘱作废失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="TransfusionDoctorAdvice", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String TransfusionDoctorAdvice(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = transfusionDoctorAdvice.transfusionDoctorAdvice(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("TransfusionDoctorAdvice：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("输血医嘱新增失败;【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="PatientRegistry", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String PatientRegistry(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = patientRegistry.patientRegistry(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("PatientRegistry：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("患者信息新增回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="PatientUpdate", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String PatientUpdate(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = patientUpdate.patientUpdate(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("PatientUpdate：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("患者信息更新回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="OptArrange", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String OptArrange(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optArrange(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("OptArrange：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("手术安排回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="OptArrangeCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String OptArrangeCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optArrangeCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("OptArrangeCancel：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("取消手术安排回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="OptComplete", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String OptComplete(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optComplete(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("OptComplete：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("手术完成通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="OptCompleteCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String OptCompleteCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optCompleteCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("OptCompleteCancel：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("取消手术完成通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="ExmRequestExecuted", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String ExmRequestExecuted(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = exmRequestExecuted.exmRequestExecuted(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("ExmRequestExecuted：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("检查登记执行通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="ExmRequestExecutedCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String ExmRequestExecutedCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = exmRequestExecutedCancel.exmRequestExecutedCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("ExmRequestExecutedCancel：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("取消检查登记执行通知回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="ExmReportAudit", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String ExmReportAudit(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = exmReportAuditORCancel.exmReportAudit(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("ExmReportAudit：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("报告回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="ExmReportAuditCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String ExmReportAuditCancel(@WebParam(name="param") String param) {
        String type = "";
        param = param.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = exmReportAuditORCancel.exmReportAuditCancel(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("ExmReportAuditCancel：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("报告回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="EMRDocumentRegistry", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String EMRDocumentRegistry(@WebParam(name="param") String param) {
        String type = "";
        param = param.replaceAll("<?xml version=\"1.0\" encoding=\"utf-8\"?>","").trim();
//        param = param.replaceAll("<!\\[CDATA\\[","").replaceAll("]]>","").trim();
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = emrDocumentRegistry.registry(paramMap);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("EMRDocumentRegistry：异常【"+e.getMessage()+"】; param:【"+param+"】");
            responseEntity = ResultMessage.error("报告回写HIS异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/responseBsxml.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }
}
