package com.soft.api.service.lis.shengguke;


import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.mapper.shengguke.EMRDocumentRegistryMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 检验报告保存 <br>
 * 样本类型--【l_jytmxx、l_plant_result、l_bio_result、l_anti_result】必须关联<br>
 * 省骨科的ANTIIT、BIOID 查询的是dic字典表<br>
 * @ClassName EMRDocumentRegistryIMPL
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/25
 * @Version V1.0
 **/
@Service //emrDocumentRegistryLISSGKImpl
public class EMRDocumentRegistryLISImpl {
    private final Logger logger = LoggerFactory.getLogger(EMRDocumentRegistryLISImpl.class);

    @Autowired
    private EMRDocumentRegistryMapper emrDocumentRegistryMapper;

//    @LcnTransaction //分布式事务propagation = DTXPropagation.REQUIRED
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity emrDocumentRegistry(String paramJson) throws Exception {
        logger.debug("【emrDocumentRegistry检验】入参：【"+paramJson+"】");
        logger.info("【emrDocumentRegistry检验】入参：【"+paramJson+"】");
        Map<String,Object> map = (Map<String,Object>) JSONArray.parse(paramJson);
        Map InspectionTestRecordMap = (Map) map.get("InspectionTestRecord");
        Map bioResultMap = (Map)InspectionTestRecordMap.get("BioResult");
        if(bioResultMap == null || bioResultMap.size() == 0){
            //一般检验
            /**
             * L_TESTDESCRIBE TESTID 需要扩数据库字段长度<br>
             * L_TESTRESULT TESTID 需要扩数据库字段长度<br>
             * L_TESTRESULT_EDIT TESTID 需要扩数据库字段长度<br>
             */
            return generalTestResult(map);
        }else{
            //微生物检验
            return microbeTestResult(map);
        }
    }

    /**
     * 微生物检验结果
     * @param map
     * @return
     */
//    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity microbeTestResult(Map<String,Object> map) throws Exception {
        ResponseEntity responseEntity = null;
        // 设置 DCOTADVISENO 值
        map.put("DOCTADVISENO", this.getBarcodeNo(map, true));
        // 设置 ELECTRONICREQUESTNOTEID 值
        map.put("ELECTRONICREQUESTNOTEID", this.getElectronicRequestNoteId(map));
        Map inspectionTestRecordMap = (Map)map.get("InspectionTestRecord");
        String sampleNo =  inspectionTestRecordMap.get("SampleNo").toString();
        map.put("SampleNo" , sampleNo);
        int relInt = emrDocumentRegistryMapper.isReview(map);
        if(relInt < 1){
            Map sampleTypeMap = (Map)inspectionTestRecordMap.get("SampleType");
            String sampleType = sampleTypeMap.get("#text").toString();
            if(sampleType.substring(0,1).equals("0")){
                sampleType = sampleType.substring(1, sampleType.length());
            }
            map.put("SAMPLETYPE" , sampleType);
            emrDocumentRegistryMapper.updateJytmxxSampleType(map);
            String barcodeNo =  this.getBarcodeNo(map, false);
            emrDocumentRegistryMapper.deletePatientInfo(map);
            HashMap HashMapPara = emrDocumentRegistryMapper.getJyTmXx(map);
            if(HashMapPara == null) {
                return ResultMessage.error(500,"未查询到相关患者条码信息");
            }
//            HashMapPara.put("SAMPLENO", barcodeNo);
            HashMapPara.put("SAMPLENO", sampleNo);
            HashMapPara.put("YBID" ,1); //省骨科新增的1代表微生物
            relInt = emrDocumentRegistryMapper.insertPatientInfo(HashMapPara);
            if(relInt < 1){
                return ResultMessage.error(500,"新增【0】条患者信息");
            }
            Map<String ,Object> paramMap = new HashMap<>();
//            Map reqItemMap = (Map)inspectionTestRecordMap.get("ReqItem"); //申请项目节点
            Map reqItemMap = null;
            if(inspectionTestRecordMap.get("ReqItem") instanceof Map) {
                //单个TestResult处理
                reqItemMap = (Map)inspectionTestRecordMap.get("ReqItem");
            }else if(inspectionTestRecordMap.get("ReqItem") instanceof List){
                List<Map<String, Object>> listMap = (List<Map<String, Object>>) inspectionTestRecordMap.get("ReqItem");
                reqItemMap = (Map)listMap.get(0).get("ReqItem");
            }
            //reqItemMap.get("LabOrderItemCode")
            Map labItemCodeMap = (Map)reqItemMap.get("LabOrderItemCode");
            reqItemMap.put("TESTCODE", labItemCodeMap.get("#text"));
            Map patientMap = (Map)map.get("Patient");
            int STAYHOSPITALMODE = 0; //1 门诊 2 住院 3 体检
            if("OV".equals(patientMap.get("SourcePatientIdType"))){
                STAYHOSPITALMODE = 1;
            }else if("IV".equals(patientMap.get("SourcePatientIdType"))){
                STAYHOSPITALMODE = 2;
            }else if("HV".equals(patientMap.get("SourcePatientIdType"))) {
                STAYHOSPITALMODE = 3;
            }
            //l_plant_result
//            Map reqMap = emrDocumentRegistryMapper.getTestIDBy(reqItemMap);
//            if(reqMap == null || reqMap.size() == 0){
//                throw new Exception("需要在lis系统中维护对应的项目编号TESTID,表名【l_channel】");
//            }

            paramMap.put("TESTID", labItemCodeMap.get("#text")); //检验项目代码reqMap.get("TESTID")
            paramMap.put("SAMPLENO" , sampleNo); //样本号
            List<Map<String, Object>> listBioMap = null;
            if(inspectionTestRecordMap.get("BioResult") instanceof Map) {
                //单个TestResult处理
                listBioMap = new ArrayList<Map<String,Object>>();
                listBioMap.add((Map<String, Object>)inspectionTestRecordMap.get("BioResult"));
            }else if(inspectionTestRecordMap.get("BioResult") instanceof List){
                listBioMap = (List<Map<String, Object>>) inspectionTestRecordMap.get("BioResult");
            }
            //细菌结果
            /**********省骨科区分有菌还是无菌，采用Remarks节点*********************/
            /********** 0 非培养类普通 1细菌培养阴性 2 致病菌  3 正常菌种 4 涂片结果 *****/
            Map<String, Object> bioMap  = listBioMap.get(0);
            String resultTypeName = "";
            if("1".equals((String)bioMap.get("Remarks"))){
                //说明微生物阴性
                paramMap.put("RESULTTYPE" , 0); //无菌
                resultTypeName = "无菌";
            }else if("2".equals(bioMap.get("Remarks").toString())
                    || "4".equals(bioMap.get("Remarks").toString())
                    || "3".equals(bioMap.get("Remarks").toString())){
                paramMap.put("RESULTTYPE" , 1); //有菌
                resultTypeName = "有"+listBioMap.size()+"菌";
            }else if("0".equals(bioMap.get("Remarks").toString())){
                throw new Exception("微生物检查不应该出现【0 非培养类普通】");
            }else {
                throw new Exception("微生物检查区分不出来有菌/无菌,【Remarks】节点传参有误,【"+bioMap.get("Remarks")+"】");
            }
            /**
             * 1、plantresult
             */
            Map docMap = (Map)inspectionTestRecordMap.get("Doctor");
            Map deptMap = (Map)inspectionTestRecordMap.get("ReportDept");
            paramMap.put("TESTRESULT" , reqItemMap.get("LabOrderItemName").toString()
                    +":"+ resultTypeName); //培养结果
            paramMap.put("SampleTypeName" , inspectionTestRecordMap.get("SampleTypeName"));
//            Map<String, Object> sampleTypeMap = emrDocumentRegistryMapper.getSampleType(paramMap);
//            if(sampleTypeMap == null || sampleTypeMap.size() == 0){
//                throw new Exception("需要在lis系统中维护样本类型表名【L_SAMPLETYPE】");
//            }
//            paramMap.put("SAMPLETYPE" , sampleTypeMap.get("SAMPLETYPE")); //样本类型

            paramMap.put("SAMPLETYPE" ,  sampleType);
            paramMap.put("STAYHOSPITALMODE",STAYHOSPITALMODE);
            int isResult = emrDocumentRegistryMapper.isPlantResult(paramMap);
            if(isResult > 0){ //表示已经有微生物结果,直接更新
                int rows = emrDocumentRegistryMapper.updatePlantResult(paramMap);
                if(rows < 1){
                    throw new Exception("更新微生物【plant_result】失败,更新数为【0】");
                }
            }else{ //新增
                //封装参数
                paramMap.put("REPORTDATE" , inspectionTestRecordMap.get("ReportTime").toString()
                        .replaceAll("T"," ").split("\\.")[0]); //报告日期
                paramMap.put("LABDEPARTMENT", deptMap.get("#text")); //检测科室
                paramMap.put("OPERATOR", docMap.get("#text")); //检测医生
                paramMap.put("INFANTFLAG" , 0); //婴儿标志 0- 非 1- 是
                int rows = emrDocumentRegistryMapper.insertPlantResult(paramMap);
                if(rows < 1){
                    throw new Exception("新增微生物【plant_result】失败,新增数为【0】");
                }
            }
            /**
             * 2、l_bio_result 细菌结果
             * 有多少菌就写多少条记录
             */
            if("1".equals(paramMap.get("RESULTTYPE").toString())){ //有菌写细菌结果表
                for(Map<String,Object> mp : listBioMap){
                    //报告日期
                    mp.put("REPORTDATE" , inspectionTestRecordMap.get("ReportTime").toString()
                            .replaceAll("T"," ").split("\\.")[0]);
                    mp.put("LABDEPARTMENT", deptMap.get("#text")); //检测科室
                    mp.put("OPERATOR", docMap.get("#text")); //检测医生
                    mp.put("SAMPLENO" , sampleNo);
                    mp.put("SAMPLETYPE" , sampleType); //样本类型
                    mp.put("STAYHOSPITALMODE",STAYHOSPITALMODE);
                    mp.put("INFANTFLAG" , 0); //婴儿标志 0- 非 1- 是
                    Map bioIDMap = (Map)mp.get("BioId");
                    mp.put("TESTCODE", bioIDMap.get("#text"));
//                    Map reqMap = emrDocumentRegistryMapper.getTestIDByBIO(mp);
                    Map reqMap = emrDocumentRegistryMapper.getBioID(mp);
                    if(reqMap == null || reqMap.size() == 0){
                        throw new Exception("需要在lis系统中维护对于的项目编号TESTID,表名【l_bio_dict】");
                    }
                    mp.put("BIOID" , reqMap.get("TESTID"));
                    isResult = emrDocumentRegistryMapper.isBioResult(mp);
                    if(isResult > 0){
                        //更新细菌结果
                        int rows = emrDocumentRegistryMapper.updateBioResult(mp);
                        if(rows < 1){
                            throw new Exception("更新微生物【l_bio_result】失败,更新数为【0】");
                        }
                    }else{
                        //新增细菌结果
                        int rows = emrDocumentRegistryMapper.insertBioResult(mp);
                        if(rows < 1){
                            throw new Exception("新增微生物【l_bio_result】失败,更新数为【0】");
                        }
                    }
                }
                /**
                 * 3、l_anti_result 药敏结果
                 */
                if( inspectionTestRecordMap.get("AntiResults") != null){ //如果有药敏结果写药敏结果表
                    List<Map<String, Object>> listAnitMap = null;
                    if(inspectionTestRecordMap.get("AntiResults") instanceof Map) {
                        //单个TestResult处理
                        listAnitMap = new ArrayList<Map<String,Object>>();
                        listAnitMap.add((Map<String, Object>)inspectionTestRecordMap.get("AntiResults"));
                    }else if(inspectionTestRecordMap.get("AntiResults") instanceof List){
                        listAnitMap = (List<Map<String, Object>>) inspectionTestRecordMap.get("AntiResults");
                    }
                    for(Map<String,Object> mp : listAnitMap){
                        mp.put("SAMPLENO" , sampleNo);
                        Map bioIDMap = (Map)mp.get("BioId");
                        mp.put("TESTCODE", bioIDMap.get("#text"));
                        Map reqMap = emrDocumentRegistryMapper.getBioID(mp);
                        //reqMap = emrDocumentRegistryMapper.getTestIDByBIO(mp);
                        if(reqMap == null || reqMap.size() == 0){
                            throw new Exception("需要在lis系统中维护对于的项目编号TESTID,表名【l_bio_dict】");
                        }
                        mp.put("BIOID" , reqMap.get("TESTID"));

                        mp.put("CHINESENAME", mp.get("AntiName"));
                        mp.put("STAYHOSPITALMODE",STAYHOSPITALMODE);

                        Map antiIdMap = (Map)mp.get("AntiId");
                        mp.put("TESTCODE", antiIdMap.get("#text"));

                        Map<String,Object> antiMap = emrDocumentRegistryMapper.getAntiID(mp);
                        if(antiMap == null || antiMap.size() == 0){
                            throw new Exception("未能在【l_anti_dict】中查询出对于的数据,请维护");
                        }
                        mp.put("ANTIID", antiMap.get("ANTIID").toString());
                        mp.put("SAMPLETYPE" , sampleType); //样本类型 sampleTypeMap.get("SAMPLETYPE")
                        if(mp.get("AntiResult").toString().indexOf("敏感") != -1) {
                            mp.put("TESTRESULT", "S");
                        }else if(mp.get("AntiResult").toString().indexOf("中介") != -1) {
                            mp.put("TESTRESULT", "I");
                        }else if(mp.get("AntiResult").toString().indexOf("耐药") != -1) {
                            mp.put("TESTRESULT", "R");
                        }else {
                            mp.put("TESTRESULT", mp.get("AntiResult"));
                        }
                        mp.put("LABDEPARTMENT", deptMap.get("#text")); //检测科室
                        mp.put("OPERATOR", docMap.get("#text")); //检测医生
                        mp.put("REPORTDATE" , inspectionTestRecordMap.get("ReportTime").toString()
                                .replaceAll("T"," ").split("\\.")[0]); //报告日期
//                        mp.putAll(antiMap);
                        isResult = emrDocumentRegistryMapper.isAntiResult(mp);
                        if(isResult > 0){
                            //更新药敏结果
                            int rows = emrDocumentRegistryMapper.updateAntiResult(mp);
                            if(rows < 1){
                                throw new Exception("更新微生物【l_anti_result】失败,更新数为【0】");
                            }
                        }else{
                            //新增药敏结果
                            int rows = emrDocumentRegistryMapper.insertAntiResult(mp);
                            if(rows < 1){
                                throw new Exception("新增微生物【l_anti_result】失败,更新数为【0】");
                            }
                        }
                    }
                }
            }
            //反更新jytmxx中sampleno
            int rel = emrDocumentRegistryMapper.updateJytmxxSampleNo(HashMapPara);
            if(rel < 1){
                throw new Exception("微生物报告反更新【JYTMXX】样本类型失败");
            }
            responseEntity = ResultMessage.success(200, "检验报告微生物报告保存成功");
        }else{
            responseEntity = ResultMessage.error(500,"检验报告微生物已审核,不能再保存");
        }
        return responseEntity;
    }

    /**
     * 一般检验结果
     * @param map
     * @return
     */
//    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity generalTestResult(Map<String,Object> map) throws Exception {
        ResponseEntity responseEntity = null;
        // 设置 DCOTADVISENO 值
        map.put("DOCTADVISENO", this.getBarcodeNo(map, true));
        // 设置 ELECTRONICREQUESTNOTEID 值
        map.put("ELECTRONICREQUESTNOTEID", this.getElectronicRequestNoteId(map));
        Map inspectionTestRecordMap = (Map) map.get("InspectionTestRecord");
        String sampleNo =  inspectionTestRecordMap.get("SampleNo").toString();
        map.put("SampleNo" , sampleNo);
        int relInt = emrDocumentRegistryMapper.isReview(map);
        if(relInt < 1){
            Map sampleTypeMap = (Map)inspectionTestRecordMap.get("SampleType");
            String sampleType = sampleTypeMap.get("#text").toString();
            if(sampleType.substring(0,1).equals("0")){
                sampleType = sampleType.substring(1, sampleType.length());
            }
            map.put("SAMPLETYPE" , sampleType);
            emrDocumentRegistryMapper.updateJytmxxSampleType(map);
            String barcodeNo =  this.getBarcodeNo(map, false);
            emrDocumentRegistryMapper.deletePatientInfo(map);
            HashMap HashMapPara = emrDocumentRegistryMapper.getJyTmXx(map);
            if(HashMapPara == null) {
                return ResultMessage.error(500,"未查询到相关患者条码信息");
            }
            HashMapPara.put("SAMPLENO", sampleNo);
            relInt = emrDocumentRegistryMapper.insertPatientInfo(HashMapPara);
            if(relInt < 1){
                return ResultMessage.error(500,"新增【0】条患者信息");
            }
            List<Object> testIds = this.getTestId(map);
            if(testIds == null){
                return ResultMessage.error(417,"检验结果不能为空");
            }
            map.put("TESTID", testIds);
            map.put("BARCODENO", sampleNo);
            emrDocumentRegistryMapper.delTestResult(map);
            Map<String, Object> paraMap = emrDocumentRegistryMapper.queryEMRDocumentRegistryParaD(map);
            if(paraMap == null){
                return ResultMessage.error(417,"添加一般结果参数为空");
            }
            paraMap.put("SAMPLETYPE" ,sampleType);
            List<Map<String,Object>> newParaMap = this.getAddParaD(map, paraMap);
            int addRowsD = 0;
            if(newParaMap != null && newParaMap.size() > 0){
                addRowsD = emrDocumentRegistryMapper.addEMRDocumentRegistryD(newParaMap);
            }else{
                return ResultMessage.error(417,"添加一般结果参数为空");
            }
            if(addRowsD < 1) {
                return ResultMessage.error(500,"新增【0】条检验结果信息");
            }
            List<String> testId = emrDocumentRegistryMapper.queryTestId(map);
            if(testId != null && testId.size() > 0){
                List<Map<String,Object>> map2List = new ArrayList<Map<String,Object>>();
                map2List.addAll(newParaMap);
                int size = newParaMap.size();
                for (int j = size-1; j >= 0; j--) {
                    for (int i = 0; i < testId.size(); i++) {
                        if(newParaMap.get(j).get("TESTID").equals(testId.get(i))){
                            map2List.remove(j);
                            continue;
                        }
                    }
                }
                if(map2List != null && map2List.size() > 0){
                    int rows = emrDocumentRegistryMapper.addLtestdescribe(map2List);
                    if(rows < 1) {
                        return ResultMessage.error(500,"新增【0】条检验结果描述信息");
                    }
                }
            }else{
                int rows = emrDocumentRegistryMapper.addLtestdescribe(newParaMap);
                if(rows < 1){
                    return ResultMessage.error(500,"新增【0】条检验结果描述信息");
                }
            }
            int rel = emrDocumentRegistryMapper.updateJytmxxSampleNo(HashMapPara);
            if(rel < 1){
                throw new Exception("检验报告反更新【JYTMXX】样本类型失败");
            }
            responseEntity = ResultMessage.error(200,"检验报告保存成功");
        }else{
            responseEntity = ResultMessage.error(500,"检验报告已审核,不能再保存");
        }
        return responseEntity;
    }






    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, Object>> getAddParaD(Map map, Map paraMap){
        List<Map<String, Object>> newListMap = new ArrayList<Map<String, Object>>();
        Map InspectionTestRecordMap = (Map) map.get("InspectionTestRecord");
        String BarcodeNo = (String) InspectionTestRecordMap.get("BarcodeNo");
        List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
        if(InspectionTestRecordMap.get("TestResults") instanceof Map) {
            //单个TestResult处理
            listMap.add((Map<String, Object>)InspectionTestRecordMap.get("TestResults"));
        }else if(InspectionTestRecordMap.get("TestResults") instanceof List){
            listMap = (List<Map<String, Object>>) InspectionTestRecordMap.get("TestResults");
        }
        for (Map<String, Object> hashMap : listMap) {
            HashMap newMap = new HashMap<String, Object>();
            newMap.putAll(paraMap);
            //传入l_testresult中operator为传入参数ReportReviewDoctor
            Map operMap = (Map)InspectionTestRecordMap.get("ReportReviewDoctor");
            if(operMap == null) {
                newMap.put("OPERATOR", "sys");
            }else {
                newMap.put("OPERATOR", operMap.get("#text"));
            }
            Map TestId = (Map) hashMap.get("TestId");
            newMap.put("SAMPLENO", InspectionTestRecordMap.get("SampleNo").toString());
            newMap.put("TESTID", TestId.get("#text"));
            newMap.put("TESTRESULT", hashMap.get("TestResult"));
            newMap.put("UNIT", hashMap.get("ResultUnit"));
            newMap.put("TESTNAME", hashMap.get("TestName"));
            newMap.put("LABDEPARTMENT", "3083,");
            newMap.put("HINT" , hashMap.get("ResultMessage"));
//            List<String> list = this.getReferenceRange(hashMap);
//            if(list.size() == 2){
//                newMap.put("REFLO", list.get(0));
//                newMap.put("REFHI", list.get(1));
//            }else if(list.size() == 1){
//                newMap.put("REFLO", list.get(0));
//            }
            newMap.put("REFLO", hashMap.get("ReferenceLow"));
            newMap.put("REFHI", hashMap.get("ReferenceHeight")); //
            newListMap.add(newMap);
        }
        return newListMap;
    }

    public List<String> getReferenceRange(Map<String, Object> map){
        List<String> list = new ArrayList<String>();
        String referenceRange = map.get("ReferenceRange").toString().trim();
        String[] strs = null;
        if(StringUtils.isEmpty(referenceRange)){
            list.add("");
            list.add("");
        }else{
            int index = referenceRange.indexOf("-");
            if(index != -1){
                strs = referenceRange.split("-");
                list = Arrays.asList(strs);
            }else{
                list.add(referenceRange);
                list.add("");
            }
        }
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Object> getTestId(Map map) throws Exception{
        Map InspectionTestRecordMap = (Map) map.get("InspectionTestRecord");
        List<Object> TestIdList = new ArrayList<>();
        if(InspectionTestRecordMap.get("TestResults") instanceof List){
            List<Map<String, Object>> listMap = (List<Map<String, Object>>) InspectionTestRecordMap.get("TestResults");
            if(listMap != null && listMap.size() > 0){
                for (Map<String, Object> hashMap : listMap) {
                    Map TestIdMap = (Map) hashMap.get("TestId");
                    TestIdList.add(TestIdMap.get("#text"));
                }
            }else{
                return null;
            }
        }else{
            Map<String, Object> hashMap = (Map<String, Object>) InspectionTestRecordMap.get("TestResults");
            if(hashMap == null){
                return null;
            }
            Map TestIdMap = (Map) hashMap.get("TestId");
            TestIdList.add(TestIdMap.get("#text"));
        }
        return TestIdList;
    }


    public String getBarcodeNo(Map map, boolean bool){
        Map InspectionTestRecordMap = (Map) map.get("InspectionTestRecord");
        if(bool){
            StringBuffer sb = new StringBuffer("DSF");
            sb.append((String)InspectionTestRecordMap.get("BarcodeNo"));
            sb.append("A");
            return sb.toString();
        }else{
            return (String)InspectionTestRecordMap.get("BarcodeNo");
        }
    }

    @SuppressWarnings("rawtypes")
    public String getElectronicRequestNoteId(Map map){
        Map InspectionTestRecordMap = (Map) map.get("InspectionTestRecord");

        Map reqMap = null;
        if(InspectionTestRecordMap.get("ReqItem") instanceof Map) {
            //单个TestResult处理
            reqMap = (Map)InspectionTestRecordMap.get("ReqItem");
//            return reqMap.get("RequestId").toString();
        }else if(InspectionTestRecordMap.get("ReqItem") instanceof List){
            List<Map<String, Object>> listMap = (List<Map<String, Object>>) InspectionTestRecordMap.get("ReqItem");
            reqMap = (Map)listMap.get(0);
        }
        return reqMap.get("RequestId").toString();
    }

}
