package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IPatientUpdateImplMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者基本信息更新
 * @ClassName PatientUpdateImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/27
 * @Version V1.0
 **/
@Service
public class PatientUpdateImpl {

    @Autowired
    private IPatientUpdateImplMapper patientUpdateImplMapper;

    @Autowired
    private com.soft.api.mapper.portal.shengguke.IPatientUpdateImplMapper protalPatientUpdateImplMapper;

    /**
     * 更新患者基本信息
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity patientUpdate(Map<String,Object> map) throws Exception{
        Map<String,Object> patientMap = (Map<String,Object>)map.get("Patient");

        //新人员注册
        Map<String,Object> paramMap = new HashMap<>();
        Map<String,Object> sex = (Map<String,Object>)patientMap.get("Sex");
        paramMap.put("Name", patientMap.get("Name"));
        paramMap.put("JGID", "1");
        paramMap.put("Sex", sex.get("#text"));
        Map<String,Object>  idCardCodeMap= (Map<String,Object>)patientMap.get("IdCardCode");
        paramMap.put("IdCardCode", idCardCodeMap.get("#text"));
        paramMap.put("YGDM", 15); //patientMap.get("YGDM")
        paramMap.put("IdCard" , patientMap.get("IdCard"));
        paramMap.put("BirthDate", patientMap.get("BirthDate"));
        paramMap.put("SystemTime" , patientMap.get("SystemTime").toString().replaceAll("T"," "));
        paramMap.put("jdr", 15);
//        paramMap.put("zxbz", 0);
        paramMap.put("PatientPhone" , patientMap.get("PatientPhone"));

        Object medicalInsuranceCategoryCodeObject = patientMap.get("MedicalInsuranceCategoryCode");
        if(medicalInsuranceCategoryCodeObject == null){
            Map brzxMap = patientUpdateImplMapper.getDefaultBRXZ(null);
            if(brzxMap == null || brzxMap.size() ==0){
                throw new Exception("未查询出系统参数【MRXZ】,联系管理员维护");
            }
            paramMap.put("BRXZ" , brzxMap.get("CSZ"));
        }else{
            Map<String,Object> medicalInsuranceCategoryCodeMap = (Map<String,Object>) medicalInsuranceCategoryCodeObject;
            paramMap.put("BRXZ" , medicalInsuranceCategoryCodeMap.get("#text"));
        }
        /**
         *  1、健康卡 有效
         *  2、市民卡 有效
         *  3、社保卡 有效
         *  4、自费卡 有效
         *  5、银联卡 有效
         *  6、电子健康卡二维码 有效
         *  99、其他卡 有效
         *
         */
        List<Map<String,Object>> cardList = null;
        Object objCard = patientMap.get("Card");
        if(objCard != null ){
            if( objCard instanceof Map) {
                cardList = new ArrayList<Map<String,Object>>();
                cardList.add((Map<String,Object>)objCard);
            }else {
                cardList = (List<Map<String,Object>>)patientMap.get("Card");
            }
            for(Map<String,Object> m : cardList){
                if("1".equals(m.get("CardTypeCode").toString())){ //健康卡
                    paramMap.put("HEALTHID" , m.get("CardNo"));
                }else if("3".equals(m.get("CardTypeCode").toString())){ //社保卡
                    paramMap.put("SBHM" , m.get("CardNo"));
                }
            }
        }
        List<Map<String,Object>> addressList = null;
        if(addressList != null){
            Object objAddress = patientMap.get("Address");
            if( objAddress instanceof Map) {
                addressList = new ArrayList<Map<String,Object>>();
                addressList.add((Map<String,Object>)objAddress);
            }else {
                addressList = (List<Map<String,Object>>)patientMap.get("Address");
            }
            paramMap.put("Address", addressList == null ? "" : addressList.get(0).get("Address"));
        }
        List<Map<String,Object>> contactList = null;
        Object objContact = patientMap.get("Contact");
        if(objContact != null){
            if( objContact instanceof Map) {
                contactList = new ArrayList<Map<String,Object>>();
                contactList.add((Map<String,Object>)objContact);
            }else {
                contactList = (List<Map<String,Object>>)patientMap.get("Contact");
            }
            paramMap.put("ContactPerson", contactList == null ? "": contactList.get(0).get("ContactPerson"));
            paramMap.put("ContactPersonTel",contactList == null ? "": contactList.get(0).get("ContactPersonTel"));
        }
        int countExistEmployee = patientUpdateImplMapper.getExistEmployeeByIDCard(paramMap);
        if(countExistEmployee < 1 ) {
            throw new Exception("该患者不存在,调用[新增个人基本信息]!");
        }
        int relInt = patientUpdateImplMapper.updateBrda(paramMap);
        if(relInt == 0) {
            throw new Exception("更新患者信息【MS_BRDA】失败,修改记录数【0】,参数【"+paramMap+"】");
        }
        relInt = patientUpdateImplMapper.updateKHBrda(paramMap);
        if(relInt == 0) {
            throw new Exception("更新患者信息【KH_BRDA】失败,修改记录数【0】,参数【"+paramMap+"】");
        }
        relInt = protalPatientUpdateImplMapper.updateGYBrda(paramMap);
        if(relInt == 0) {
            throw new Exception("更新患者信息【GY_BRDA】失败,修改记录数【0】,参数【"+paramMap+"】");
        }
        return ResultMessage.success(200,"修改患者信息成功");
    }
}
