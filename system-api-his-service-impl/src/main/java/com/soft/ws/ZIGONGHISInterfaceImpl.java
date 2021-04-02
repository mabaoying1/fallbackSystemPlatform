package com.soft.ws;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.service.his.zigong.DepatmentImpl;
import com.soft.api.service.his.zigong.EmployeeImpl;
import com.soft.api.service.his.zigong.OperationImpl;
import ctd.net.rpc.util.FreemarkerUtil;
import ctd.net.rpc.util.ObjectToMap;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.Map;

/**
 * 自贡HIS接口
 * @ClassName ZIGONGHISInterfaceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/23
 * @Version V1.0
 **/

//@WebService(
//        targetNamespace="http://ws.soft.com", //命名空间,一般是接口的包名
//        name="IHISInterface",
//        serviceName="WeatherWS"   //该webservice服务的名称
//)//        portName="WeatherWSSoapPort",
public class ZIGONGHISInterfaceImpl implements IZIGONGHISInterface {

    @Autowired
    private DepatmentImpl depatment;

    @Autowired
    private EmployeeImpl employee;

    @Autowired
    private OperationImpl operation;

    @WebMethod(
            operationName="DepatmentAdd", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String DepatmentAdd(@WebParam(name="param") String param){
        String type = "";
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            int relCount =depatment.saveDepatment((Map<String, Object>)paramMap.get("Office"));
            if(relCount == 1){
                responseEntity = ResultMessage.success("保存科室信息成功");
            }else{
                responseEntity = ResultMessage.error("保存科室信息失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("保存科室信息异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="DepatmentUpdate", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String DepatmentUpdate(@WebParam(name="param") String param) {

        String type = "";
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            int relCount =depatment.modifyDepatment((Map<String, Object>)paramMap.get("Office"));
            if(relCount == 1){
                responseEntity = ResultMessage.success("修改科室信息成功");
            }else{
                responseEntity = ResultMessage.error("修改科室信息失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("修改科室信息异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="EmployeeAdd", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String EmployeeAdd(@WebParam(name="param") String param) {
        String type = "";
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            int relCount = employee.saveEmployee((Map<String, Object>)paramMap.get("Personnel"));
            if(relCount == 1){
                responseEntity = ResultMessage.success("保存人员信息成功");
            }else{
                responseEntity = ResultMessage.error("保存人员信息失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("保存人员信息异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="EmployeeUpdate", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String EmployeeUpdate(@WebParam(name="param") String param) {
        String type = "";
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            int relCount = employee.modifyEmployee((Map<String, Object>)paramMap.get("Personnel"));
            if(relCount == 1){
                responseEntity = ResultMessage.success("修改人员信息成功");
            }else{
                responseEntity = ResultMessage.error("修改人员信息失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("修改人员信息异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
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
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optArrange((Map<String, Object>)paramMap.get("OptRequest"));
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("手术安排通知异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
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
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.optArrangeCancel((Map<String, Object>)paramMap.get("OptRequest"));
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("取消手术安排通知异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
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
        ResponseEntity responseEntity = null;
        try {
            type = ObjectToMap.getObjectType(param);
            Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
            responseEntity = operation.operationCompleted((Map<String, Object>)paramMap.get("OptRequest"));
        }catch (Exception e) {
            e.printStackTrace();
            responseEntity = ResultMessage.error("手术完成通知异常【"+e.getMessage()+"】");
        }
        if(type.equals("XML")){
            return FreemarkerUtil.castXML("templates/response.ftl", responseEntity);
        }
        return JSONObject.toJSONString(responseEntity);
    }

    @WebMethod(
            operationName="OptCompleteCancel", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String OptCompleteCancel(@WebParam(name="param") String param) {
        return null;
    }


//    @WebMethod(
//            operationName="outPatientRegisteredQuery", //发布的方法名称
//            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
//    )//WebResult :返回值名称 WebParam:参数名称
//    //public @WebResult(name="result")String queryWeather(@WebParam(name="cityName")String cityName)
//    @Override
//    public String outPatientRegisteredQuery(@WebParam(name="param")String param) {
//
//        return null;
//    }
}
