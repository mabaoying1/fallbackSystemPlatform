package com.soft.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soft.Entity.DrgsParameterEntity;
import com.soft.Entity.ResponseEntity;
import com.soft.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * @ClassName DAZhouDrgsController
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
@RestController
public class DAZHOUDrgsController {
   private final Logger logger = LoggerFactory.getLogger(DAZHOUDrgsController.class);

   @Autowired
   private CallHttpPostWebservice callHttpPostWebservice;

   @Autowired
   private CallAxisWebservice callAxisWebservice;

   @Autowired
   private DrgsParameterEntity drgsParameterEntity;

    /**
     * 封装数据并上传至国信健康医保平台
     * @param serviceId
     * @param zyh
     * @param data
     * @return
     */
   public String drgsPackageUpload(String serviceId,String zyh,String zyhm ,String data){
       try {
           if(zyh == null || zyh.isEmpty()){
               return JSONObject.toJSONString(ResultMessage.error(401,"参数[zyh],不能为空"));
           }
           if(serviceId == null || serviceId.isEmpty()){
               return JSONObject.toJSONString(ResultMessage.error(401,"参数[serviceID],不能为空"));
           }
           String urlAddr = drgsParameterEntity.getOdinAddr();
           String nameSpaceURI = drgsParameterEntity.getOdinNameSpace();
           String method = drgsParameterEntity.getOdinMethod();
           String[] paramType = {"string","string","string","string"};
           String[] paramName = {"zyh","zyhm","serviceID","data"};
           String returnType = "string";
           switch (serviceId){
               case "5001" : //病案信息上传,一次住院的所有病案信息上传
                   ResponseEntity responseEntity = operator5001(zyh,zyhm,data,urlAddr,nameSpaceURI,method,paramType,paramName,returnType);
                   if(responseEntity.getCode() == 200){
                       data = responseEntity.getResult().toString();
                   }else{
                       return JSONObject.toJSONString(responseEntity);
                   }
                   break;
               case "5002" : //病案信息查询、作废
               case "5003" :
                   data = "{\"AdmissionNo\": \""+zyh+"\", " +
                           "\"HospitalId\": \"021001\",\"data\":\"\" }";
                   break;
               case "6001" : //门诊病案信息上传
                   data = operator6001(zyh,urlAddr,nameSpaceURI,method,paramType,paramName,returnType);
                   break;
               case "6002" : //门诊病案信息查询、作废
               case "6003" :
                   data = "{\"PatientNo\": \""+zyh+"\", " +
                           "\"HospitalId\": \"021001\",\"data\":\"\"}";
               break;
           }
           String uuidStr = UUIDUtil.getUUID();
           MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
           String md5String = "";
           if(drgsParameterEntity.getSiginMethod().equals("1")){
               md5String = MD5Util.dzMd5(drgsParameterEntity.getToken()
                       ,drgsParameterEntity.getUserId());
           }else{
               md5String = MD5Util.dzMd5(data + uuidStr + drgsParameterEntity.getToken()
                       + drgsParameterEntity.getUserId());
           }
           param.set("serviceId", serviceId); //服务ID
           param.set("userId", drgsParameterEntity.getUserId()); //userId
           param.set("siginMethod", drgsParameterEntity.getSiginMethod()); //1：简单的安全校验 2：复杂的安全校验
           param.set("signData",md5String);
           param.set("nonce",uuidStr);// siginMethod=1 nonce参数为空 siginMethod=2 nonce参数必填，nonce为GUID
           param.set("data",data);
           return callHttpPostWebservice.callHttpPost(param,drgsParameterEntity.getUrlAddr(),"");
       }catch (Exception e){
           e.printStackTrace();
           logger.error("平台error"+e.getMessage());
           return JSONObject.toJSONString(ResultMessage.error(e.getMessage()));
       }
   }



   public ResponseEntity operator5001(String zyh ,String zyhm,String data, String urlAddr , String nameSpaceURI, String method
           , String[] paramType , String[] paramName, String returnType) throws SQLException, IOException {
       if(zyh == null || zyh.isEmpty() ){
           return ResultMessage.error(402,"serviceID[5001],必须上传zyh");
       }
       //根据zyh 去关联手麻和病案以及HIS，拼接完整的病案信息
       Object[] paramValue = new Object[]{zyh,zyhm,"ba",data};
       //1、抓取病案主页
       String medicalObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //判断病案是否有数据，有才进行下一步封装
       Object medicalVal = JSONObject.parseObject(medicalObject).get("Medical");
       if(medicalVal == null || "[]".equals(medicalVal.toString())){
           return ResultMessage.error(403,"根据住院号["+zyh+"],未查询出病案信息;");
       }
       paramValue = new Object[]{zyh,zyhm,"bafy",data};
       String medicalAttachObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //2、抓取手麻
       paramValue = new Object[]{zyh,zyhm,"ssjl",data};
       String listOperationObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //3、HIS
       //3.1 抓取出院小结
       paramValue = new Object[]{zyh,zyhm,"cyxj",data};
       String leaveHospitalObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //3.2 检查主单
       paramValue = new Object[]{zyh,zyhm,"jczd",data};
       String listCheckObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //3.3 重症監護
       paramValue = new Object[]{zyh,zyhm,"zzjh",data};
       String listICUObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
               method,paramType,paramName,returnType,paramValue);
       //拼装上传JSON
       StringBuffer buffer = new StringBuffer();
       buffer.append("{");
       //处理病案主页
       JSONArray medicalArr = (JSONArray)JSONObject.parseObject(medicalObject).get("Medical");
       if(medicalArr.size() > 0){
           buffer.append("\"Medical\":"+medicalArr.get(0));
       }else{
           buffer.append("\"Medical\":{}");
       }
       //处理病案附页
       JSONArray medicalAttach = (JSONArray)JSONObject.parseObject(medicalAttachObject).get("MedicalAttach");
       if(medicalAttach.size() > 0){
           buffer.append(",\"MedicalAttach\":"+medicalAttach.get(0));
       }else{
           buffer.append(",\"MedicalAttach\":{}");
       }
       //检查
       JSONArray listCheck = (JSONArray)JSONObject.parseObject(listCheckObject).get("ListCheck");
       buffer.append(",\"ListCheck\":"+listCheck);
       //手术
       JSONArray listOperation = (JSONArray)JSONObject.parseObject(listOperationObject).get("ListOperation");
       buffer.append(",\"ListOperation\":"+listOperation);
        //出院
       JSONArray leaveHospital = (JSONArray)JSONObject.parseObject(leaveHospitalObject).get("LeaveHospital");
       if(leaveHospital.size() > 0){
           BASE64Decoder decoder = new BASE64Decoder();
           Object DischargeOutcome = JSONObject.parseObject(leaveHospital.get(0).toString()).get("DischargeOutcome");
           Object HospitalizationSituation = JSONObject.parseObject(leaveHospital.get(0).toString()).get("HospitalizationSituation");
           byte[] bytes = decoder.decodeBuffer(HospitalizationSituation.toString());
           HospitalizationSituation = new String(bytes,"gbk").replaceAll("\"","");
//           Clob cHospitalizationSituation = new SerialClob(HospitalizationSituation.toString().toCharArray());
           Object DtProcess = JSONObject.parseObject(leaveHospital.get(0).toString()).get("DtProcess");
           bytes = decoder.decodeBuffer(DtProcess.toString());
           DtProcess = new String(bytes,"gbk").replaceAll("\"","");
//           Clob cDtProcess = new SerialClob(DtProcess.toString().toCharArray());
           Object LeaveHospitalStatus = JSONObject.parseObject(leaveHospital.get(0).toString()).get("LeaveHospitalStatus");
           bytes = decoder.decodeBuffer(LeaveHospitalStatus.toString());
           LeaveHospitalStatus = new String(bytes,"gbk").replaceAll("\"","");
//           Clob cLeaveHospitalStatus = new SerialClob(LeaveHospitalStatus.toString().toCharArray());
           Object LeaveDoctorAdvice = JSONObject.parseObject(leaveHospital.get(0).toString()).get("LeaveDoctorAdvice");
           bytes = decoder.decodeBuffer(LeaveDoctorAdvice.toString());
           LeaveDoctorAdvice = new String(bytes,"gbk").replaceAll("\"","");
//           Clob cLeaveDoctorAdvice = new SerialClob(LeaveDoctorAdvice.toString().toCharArray());
           buffer.append(",\"LeaveHospital\":{\"DischargeOutcome\":\""+DischargeOutcome
                   +"\",\"HospitalizationSituation\":\""+ HospitalizationSituation
                   +"\",\"DtProcess\":\""+ DtProcess
                   +"\",\"LeaveHospitalStatus\":\""+ LeaveHospitalStatus
                   +"\",\"LeaveDoctorAdvice\":\""+LeaveDoctorAdvice +"\"}");
////           buffer.append(",\"LeaveHospital\":"+leaveHospital.get(0));
       }else{
           buffer.append(",\"LeaveHospital\":{}");
       }
        //重症
       JSONArray listICU = (JSONArray)JSONObject.parseObject(listICUObject).get("ListICU");
       buffer.append(",\"ListICU\":"+listICU);
       buffer.append("}");
//       logger.info("拼装后参数为：【"+buffer.toString()+"】");
       return ResultMessage.success(200,"",buffer.toString());
   }

    public String operator6001(String zyh , String urlAddr ,String nameSpaceURI,String method
            ,String[] paramType , String[] paramName, String returnType){
        Object[] paramValue = new Object[]{zyh,"mzbazy"};
        String opcMedicalObject = callAxisWebservice.callAxisService(urlAddr,nameSpaceURI,
                method,paramType,paramName,returnType,paramValue);
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        //处理病案主页
        JSONArray opcMedicalArr = (JSONArray)JSONObject.parseObject(opcMedicalObject).get("OPCMedical");
        buffer.append("\"OPCMedical\":"+opcMedicalArr.get(0));
        buffer.append("}");
        return buffer.toString();
    }


}
