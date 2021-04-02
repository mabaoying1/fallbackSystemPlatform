package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IPatientRegistryMapper;
import ctd.net.rpc.util.DateTimeFormatterUtil;
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
 * 患者基本信息注册
 * @ClassName PatientRegistryImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/27
 * @Version V1.0
 **/
@Service
public class PatientRegistryImpl {

    @Autowired
    private IPatientRegistryMapper patientRegistryMapper;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private com.soft.api.mapper.portal.shengguke.IPatientRegistryMapper iPatientRegistryMapper;

    /**
     * 患者基本信息注册
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity patientRegistry(Map<String,Object> map) throws Exception {
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
        paramMap.put("jdr", 15); //patientMap.get("YGDM")
        paramMap.put("zxbz", 0);
        paramMap.put("PatientPhone" , patientMap.get("PatientPhone"));

        Object medicalInsuranceCategoryCodeObject = patientMap.get("MedicalInsuranceCategoryCode");
        if(medicalInsuranceCategoryCodeObject == null){
            Map brzxMap = patientRegistryMapper.getDefaultBRXZ(null);
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
        paramMap.put("BRID", queryHISKeyCommon.getMaxANDValide("MS_BRDA",1));
        ResponseEntity responseEntity = registry(paramMap);
        return responseEntity;
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity registry(Map<String,Object> paramMap) throws Exception{
        //根据身份证号码去查询，判断该人员是否注册。如果已注册则更新
        int countExistEmployee = patientRegistryMapper.getExistEmployeeByIDCard(paramMap);
        if(countExistEmployee > 0) {
            throw new Exception("该患者已存在,调用[个人基本信息查询]!");
        }
        Map<String,Object> lyrqMap = patientRegistryMapper.getLYRQ(paramMap);
        if(lyrqMap == null || lyrqMap.size() == 0
                || lyrqMap.get("LYRQ") == null || lyrqMap.get("LYRQ").toString() == "") {
            throw new Exception("为查询出【LYRQ】,查询参数【"+paramMap+"】");
        }
        paramMap.put("LYRQ", lyrqMap.get("LYRQ").toString().split("\\.")[0]);
        //根据领用日期+机构+员工代码
        Map<String,Object> mzhmMap = patientRegistryMapper.getMzhm(paramMap);
        if(mzhmMap == null || mzhmMap.get("MZHM") == null || mzhmMap.get("MZHM").toString() == "") {
            throw new Exception("未查询出【MZHM】,查询参数【"+paramMap+"】");
        }
        String mzhm = mzhmMap.get("MZHM").toString();
        //加一后的新门诊号码
        String mzhm_new = incrementN(mzhm, 1);
        //获取总值号码
        Map<String,Object> zzhmMap=  patientRegistryMapper.getZzhm(paramMap);
        if(zzhmMap == null || zzhmMap.get("ZZHM") == null || zzhmMap.get("ZZHM").toString() == "") {
            throw new Exception("为查询出【ZZHM】,查询参数【"+paramMap+"】");
        }
        String zzhm = zzhmMap.get("ZZHM").toString();
        paramMap.put("MZHM", mzhm_new);
        //如果当前值+1后的门诊号码   大于  总值
        if(Long.parseLong(mzhm_new) > Long.parseLong(zzhm) ) {
            int updateCount= patientRegistryMapper.updateYgpjBySypb(paramMap);
            if(updateCount == 0) {
                throw new Exception("更新员工票据失败，更新条数【0】,参数【"+paramMap+"】");
            }
            //生成可用号
            int xngh = 0;
            Map rxnghMap = patientRegistryMapper.getMzhmXngh(paramMap);
            if( rxnghMap == null ||  rxnghMap.size() == 0){
                throw new Exception("mzhm系列获取虚拟工号为空,参数【"+paramMap+"】");
            }
            xngh = Integer.parseInt(rxnghMap.get("XNGH").toString());
            if(xngh == 99){
                throw new Exception("mzhm系列获取虚拟工号值为99,已用完,参数【"+paramMap+"】");
            }
            xngh += 1;
            //@4 生成门诊号码  生成规则  yyMMdd+NXGH+001-----yyMMdd+NXGH+999 换成数据库提取 2019年10月21日
            int relInt = patientRegistryMapper.updateMzhmYgpj(paramMap);
            if(relInt < 1 ){
                throw new Exception("更新员工票据SYPB=1失败,更新记录数【0】,参数【"+paramMap+"】");
            }
            paramMap.put("XNGH", xngh);
            paramMap.put("XNGHS", xngh+"001");
            paramMap.put("XNGHE", xngh+"999");
            //新增MS_YGPJ
            relInt = patientRegistryMapper.updateMzhmXtcs(paramMap);
            if(relInt < 1 ){
                throw new Exception("更新员工票据SYPB=1失败,更新记录数【0】,参数【"+paramMap+"】");
            }
            relInt = patientRegistryMapper.insertMzhmYgpj(paramMap);
            if(relInt < 1 ){
                throw new Exception("新增【YGPJ】,更新记录数【0】,参数【"+paramMap+"】");
            }
            //重新赋值门诊号码
            mzhm_new = incrementN(xngh+"001", 1);
            paramMap.put("MZHM", mzhm_new);
        }
        //将 加一后的门诊序号更新至数据库syhm字段
        int relInt = patientRegistryMapper.updateYgpjBySyhm(paramMap);
        if(relInt == 0) {
            throw new Exception("更新员工票据使用号码失败,更新记录【0】,参数【"+paramMap+"】");
        }
        relInt = patientRegistryMapper.insertBrda(paramMap);
        if(relInt == 0) {
            throw new Exception("新增患者信息【MS_BRDA】失败,新增记录数【0】,参数【"+paramMap+"】");
        }
        relInt = patientRegistryMapper.insertKHBrda(paramMap);
        if(relInt == 0) {
            throw new Exception("新增患者信息【KH_BRDA】失败,新增记录数【0】,参数【"+paramMap+"】");
        }
        relInt = iPatientRegistryMapper.addGyBrdaPortal(paramMap);
        if(relInt == 0) {
            throw new Exception("新增患者信息【GY_BRDA】失败,新增记录数【0】,参数【"+paramMap+"】");
        }
        return ResultMessage.success(200,"新增患者信息成功");
    }



    public String incrementN(String originalValue , int incrementValue) {
        String result = "";
        if(originalValue == null ||  "".equals(originalValue)) {
            return result;
        }
        char[] charFphm = originalValue.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < charFphm.length; i++){
            if(charFphm[i] >= 'a'&& charFphm[i] <= 'z'||charFphm[i] >= 'A'&& charFphm[i] <= 'Z'){
                buffer.append(charFphm[i]);
            }
        }
        if(buffer.toString().length() > 0){
            result = buffer.toString() +
                    String.format("%0" + (originalValue.length()-buffer.toString().length() ) + "d",
                            Long.parseLong(originalValue.replace(buffer.toString(), "")) + incrementValue ) ;
        }else{
            result = String.format("%0" + originalValue.length() + "d",Long.parseLong(originalValue) + incrementValue ) ;
        }
        return result;
    }
}
