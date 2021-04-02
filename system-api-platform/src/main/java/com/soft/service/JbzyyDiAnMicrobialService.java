package com.soft.service;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.mapper.CQ.JbzyyDiAnMicrobialMapper;

import ctd.net.rpc.util.CallInterfaceUtil;
import ctd.net.rpc.util.DateTimeFormatterUtil;
import ctd.net.rpc.util.XmlToMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 〈功能概述〉<br>
 *
 * @className: JbzyyDiAnMicrobialService
 * @package: com.soft.service
 * @author: Ljx
 * @date: 2021/03/22 11:03
 */
@Slf4j
@Service
public class JbzyyDiAnMicrobialService {
    @Autowired
    private JbzyyDiAnMicrobialMapper jbzyyDiAnMicrobialMapper;

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor=Exception.class)
    public String microbial(String... args) throws Exception {
        StringBuffer warnBuff = new StringBuffer();
        String url = args[0];
        String ClientID = args[1];
        String ClientGUID = args[2];
        String method = args[3];
        String StartDate= DateTimeFormatterUtil.format(DateTimeFormatterUtil.getNextDay(new Date(), -1), "yyyy-MM-dd");
        String EndDate = DateTimeFormatterUtil.format(LocalDateTime.now(), "yyyy-MM-dd");
        StartDate = "2019-12-01"; EndDate ="2019-12-31";

//		Object resultXml = CallInterfaceUtil.call(url, method, PARAMS);
        String[] params = new String[] {ClientID,ClientGUID,StartDate,EndDate};
        List<String> parameter = new ArrayList<String>();
        parameter.add("ClientID");parameter.add("ClientGUID");parameter.add("StartDate");parameter.add("EndDate");
        log.info("调用webServiceAsmx参数【"+ClientID+";"+ClientGUID+";"+StartDate+";"+EndDate+"】");
		Object resultXml = CallInterfaceUtil.webServiceAsmx(url, "http://report.dagene.net/", params, method, parameter);
		if(resultXml == null || resultXml == "" || resultXml == "null") {
			warnBuff.append("调用method【"+method+"】,无数据！入参【"+params+"】");
		}

        //10\11\99
//        Object resultXml = "<Error><Code>0</Code><Descript>完成</Descript></Error><ResultsDataSet>" +
//                "  <Table>" +
//                "    <BARCODE>100160550715</BARCODE>" +
//                "    <MGUID>{E0497EB5-5E74-42DD-BC14-475050232E5C}</MGUID>" +
//                "    <MICROBECODE>L2019120001</MICROBECODE>" +
//                "    <TESTCODE>3000913</TESTCODE>" +
//                "    <TANKFARM>三线培养</TANKFARM>" +
//                "    <TESTDATE>2019-12-07T21:50:58+08:00</TESTDATE>" +
//                "    <REPORTDATE>2019-12-09T00:00:00+08:00</REPORTDATE>" +
//                "    <MIC_SAMPLETYPE>分泌物</MIC_SAMPLETYPE>" +
//                "    <IFMASCULINE>阴性</IFMASCULINE>" +
//                "    <PATIENTNAME>曾川桃</PATIENTNAME>" +
//                "    <PATIENTCATEGORY>门诊</PATIENTCATEGORY>" +
//                "    <SEX>女</SEX>" +
//                "    <AGE_AGEUNIT>27岁</AGE_AGEUNIT>" +
//                "    <CLINICID>9000036666999</CLINICID>" +
//                "    <CLINICNAME>妇产科</CLINICNAME>" +
//                "    <DOCTOR>文霞</DOCTOR>" +
//                "    <SAMPLEFROM>重庆市江北区中医院</SAMPLEFROM>" +
//                "    <COLLECTDDATE>2019-12-07T00:00:00+08:00</COLLECTDDATE>" +
//                "    <CHECKMANNAME>赵艳华</CHECKMANNAME>" +
//                "    <APPROVALMANNAME>刘小平</APPROVALMANNAME>" +
//                "    <CHECKBY>《全国临床检验操作规程》第四版（2015年）</CHECKBY>" +
//                "    <F50>杭州无菌1抬头模板.frf</F50>" +
//                "    <FINAL1>{" +
//                " \"type\": \"99\","+
//                "  \"value\": ["+
//                "    {"+
//                "      \"f1\": \"未检出霍乱弧菌,未检出沙门菌属及志贺菌属\","+
//                "      \"f50\": \"杭州无菌1抬头模版.frf\","+
//                "      \"pfn\": \"三线培养项目x\""+
//                "    }"+
//                "  ]"+
////				"  \"type\":\"11\","+
////				"  \"value\": [{"+
////				"	\"item\":\"真菌培养：\","+
////				"	\"rslt\":\"未培养出真菌\""+
////				"},"+
////				"{"+
////				"	\"item\":\"一般细菌培养：\","+
////				"	\"rslt\":\"培养无菌生长\""+
////				"}"+
////				"]"+
//
////				"  \"type\": \"11\"," +
////				"  \"value\": [" +
////				"    \"未培养出淋球菌\"" +
////				"  ]" +
//                "}</FINAL1>" +
//                "  </Table>" +
//                "</ResultsDataSet>";


        //20
//        resultXml = "<Error><Code>0</Code><Descript>完成</Descript></Error><ResultsDataSet>" +
//                "  <Table>" +
//                "    <BARCODE>100160550715</BARCODE>" +
//                "    <MGUID>{E0497EB5-5E74-42DD-BC14-475050232E5C}</MGUID>" +
//                "    <MICROBECODE>L2019120001</MICROBECODE>" +
//                "    <TESTCODE>890</TESTCODE>" +
//                "    <TANKFARM>支原体培养及鉴定+药敏试验</TANKFARM>" +
//                "    <TESTDATE>2019-12-07T21:50:58+08:00</TESTDATE>" +
//                "    <REPORTDATE>2019-12-09T00:00:00+08:00</REPORTDATE>" +
//                "    <MIC_SAMPLETYPE>宫颈分泌物</MIC_SAMPLETYPE>" +
//                "    <IFMASCULINE>阳性</IFMASCULINE>" +
//                "    <PATIENTNAME>曾川桃</PATIENTNAME>" +
//                "    <PATIENTCATEGORY>门诊</PATIENTCATEGORY>" +
//                "    <SEX>女</SEX>" +
//                "    <AGE_AGEUNIT>27岁</AGE_AGEUNIT>" +
//                "    <CLINICID>9000036666999</CLINICID>" +
//                "    <CLINICNAME>妇产科</CLINICNAME>" +
//                "    <DOCTOR>文霞</DOCTOR>" +
//                "    <SAMPLEFROM>重庆市江北区中医院</SAMPLEFROM>" +
//                "    <COLLECTDDATE>2019-12-07T00:00:00+08:00</COLLECTDDATE>" +
//                "    <CHECKMANNAME>赵艳华</CHECKMANNAME>" +
//                "    <APPROVALMANNAME>刘小平</APPROVALMANNAME>" +
//                "    <CHECKBY>《全国临床检验操作规程》第四版（2015年）</CHECKBY>" +
//                "    <F50>杭州无菌1抬头模板.frf</F50>" +
//                "    <FINAL1>{" +
//                "	\"type\": \"20\"," +
//                "	\"value\": [" +
//                "	  {" +
//                "	    \"item\": \"Uu\"," +
//                "	    \"rslt\": \"阳性\"," +
//                "	    \"type\": \"1\"" +
//                "	  }," +
//                "	  {" +
//                "	    \"item\": \"Mh\"," +
//                "	    \"rslt\": \"阳性\"," +
//                "	    \"type\": \"1\"" +
//                "	  }," +
//                "	  {" +
//                "	    \"item\": \"Uu计数\"," +
//                "	    \"rslt\": \"≥10^4\"," +
//                "	    \"type\": \"1\"" +
//                "	  }," +
//                "	  {" +
//                "	    \"item\": \"Mh计数\"," +
//                "	   \"rslt\": \"≥10^4\"," +
//                "	    \"type\": \"1\"" +
//                "	  }," +
//                "	  {" +
//                "	    \"item\": \"四环素\"," +
//                "	    \"rslt\": \"I\"," +
//                "	    \"type\": \"1\"" +
//                "	  }" +
//                "	  {" +
//                "	    \"item\": \"强力霉素\"," +
//                "	    \"rslt\": \"S\"," +
//                "	    \"type\": \"1\"" +
//                "	  }" +
//                "	]" +
//                "}</FINAL1>" +
//                "  </Table>" +
//                "</ResultsDataSet>";
//

        //15
//        resultXml = "<Error><Code>0</Code><Descript>完成</Descript></Error><ResultsDataSet>" +
//                "  <Table>" +
//                "    <BARCODE>100160955512</BARCODE>" +
//                "    <MGUID>{E0497EB5-5E74-42DD-BC14-475050232E5C}</MGUID>" +
//                "    <MICROBECODE>X2020030050</MICROBECODE>" +
//                "    <TESTCODE>890</TESTCODE>" +
//                "    <TANKFARM>血培养及鉴定</TANKFARM>" +
//                "    <TESTDATE>2019-12-07T21:50:58+08:00</TESTDATE>" +
//                "    <REPORTDATE>2019-12-09T00:00:00+08:00</REPORTDATE>" +
//                "    <MIC_SAMPLETYPE>全血</MIC_SAMPLETYPE>" +
//                "    <IFMASCULINE>阳性</IFMASCULINE>" +
//                "    <PATIENTNAME>曾川桃</PATIENTNAME>" +
//                "    <PATIENTCATEGORY>门诊</PATIENTCATEGORY>" +
//                "    <SEX>女</SEX>" +
//                "    <AGE_AGEUNIT>27岁</AGE_AGEUNIT>" +
//                "    <CLINICID>9000036666999</CLINICID>" +
//                "    <CLINICNAME>妇产科</CLINICNAME>" +
//                "    <DOCTOR>文霞</DOCTOR>" +
//                "    <SAMPLEFROM>重庆市江北区中医院</SAMPLEFROM>" +
//                "    <COLLECTDDATE>2019-12-07T00:00:00+08:00</COLLECTDDATE>" +
//                "    <CHECKMANNAME>赵艳华</CHECKMANNAME>" +
//                "    <APPROVALMANNAME>刘小平</APPROVALMANNAME>" +
//                "    <CHECKBY>《全国临床检验操作规程》第四版（2015年）</CHECKBY>" +
//                "    <F50>杭州无菌1抬头模板.frf</F50>" +
//                "    <FINAL1>{" +
//                "	\"type\": \"15\"," +
//                "	\"value\": [" +
//                "	{  " +
//                "      \"item\": \"溶血葡萄球菌(MRS&amp;诱导克林霉素耐药β内酰胺酶)\", " +
//                "       \"rslt\": \"\", " +
//                "      \"expert\": \"该菌对β内酰胺类/β-内酰胺酶抑制剂复合物、口服头孢类、注射头孢类包括Ⅰ$%Ⅱ$%Ⅲ和Ⅳ代、碳青霉烯类耐药，但较新的具有抗MRSA活性的头孢菌素除外。 利福平不能单独用于抗菌治疗。 呋喃妥因、洛美沙星是仅用于或首选治疗泌尿道感染的抗菌药物。 根据诱导克林霉素耐药试验，推测该菌株对克林霉素耐药，对某些病人克林霉素可能仍有效。 当使用庆大霉素治疗该菌引起的感染时，庆大霉素需同其他药敏结果为敏感的药物联用。 药敏试验中，敏感度一栏后面带有*的表示为专家修正值。\"," +
//                "      \"ym\": [ " +
//                "        {" +
//                "          \"ym-name\": \"四环素1\"," +
//                "          \"ym-value\": \"&gt;=4\"," +
//                "          \"ym-unit\": \"μg/ml\"," +
//                "          \"ym-sr\": \"耐药\"," +
//                "         \"ym-group\": \"A\"" +
//                "        }," +
//                "        {" +
//                "          \"ym-name\": \"强力霉素\"," +
//                "          \"ym-value\": \"&gt;=8\"," +
//                "          \"ym-unit\": \"μg/ml\"," +
//                "          \"ym-sr\": \"耐药\"," +
//                "          \"ym-group\": \"A\"" +
//                "        } " +
//                "      ]" +
//                "    }" +
//
//                "	]" +
//                "}</FINAL1>" +
//                "  </Table>" +
//                "</ResultsDataSet>";

        //18
//		resultXml = "<Error><Code>0</Code><Descript>完成</Descript></Error><ResultsDataSet>" +
//				"  <Table>" +
//				"    <BARCODE>100160955512</BARCODE>" +
//				"    <MGUID>{E0497EB5-5E74-42DD-BC14-475050232E5C}</MGUID>" +
//				"    <MICROBECODE>X2020030050</MICROBECODE>" +
//				"    <TESTCODE>890</TESTCODE>" +
//				"    <TANKFARM>血培养及鉴定</TANKFARM>" +
//				"    <TESTDATE>2019-12-07T21:50:58+08:00</TESTDATE>" +
//				"    <REPORTDATE>2019-12-09T00:00:00+08:00</REPORTDATE>" +
//				"    <MIC_SAMPLETYPE>全血</MIC_SAMPLETYPE>" +
//				"    <IFMASCULINE>阳性</IFMASCULINE>" +
//				"    <PATIENTNAME>曾川桃</PATIENTNAME>" +
//				"    <PATIENTCATEGORY>门诊</PATIENTCATEGORY>" +
//				"    <SEX>女</SEX>" +
//				"    <AGE_AGEUNIT>27岁</AGE_AGEUNIT>" +
//				"    <CLINICID>9000036666999</CLINICID>" +
//				"    <CLINICNAME>妇产科</CLINICNAME>" +
//				"    <DOCTOR>文霞</DOCTOR>" +
//				"    <SAMPLEFROM>重庆市江北区中医院</SAMPLEFROM>" +
//				"    <COLLECTDDATE>2019-12-07T00:00:00+08:00</COLLECTDDATE>" +
//				"    <CHECKMANNAME>赵艳华</CHECKMANNAME>" +
//				"    <APPROVALMANNAME>刘小平</APPROVALMANNAME>" +
//				"    <CHECKBY>《全国临床检验操作规程》第四版（2015年）</CHECKBY>" +
//				"    <F50>杭州无菌1抬头模板.frf</F50>" +
//				"    <FINAL1>{" +
//				"	\"type\": \"18\"," +
//				"	\"value\": [" +
//				"	{  " +
//				"      \"item\": \"产酸克雷伯菌\", " +
//				"       \"rslt\": \"计数：++\", " +
//				"      \"ym\": [ " +
//				"        {" +
//				"          \"ym-name\": \"四环素\"," +
//				"          \"ym-value\": \"&gt;=4\"," +
//				"          \"ym-unit\": \"μg/ml\"," +
//				"          \"ym-sr\": \"耐药\"," +
//				 "         \"ym-group\": \"A\"" +
//				"        }," +
//				"        {" +
//				"          \"ym-name\": \"强力霉素\"," +
//				"          \"ym-value\": \"&gt;=8\"," +
//				"          \"ym-unit\": \"μg/ml\"," +
//				"          \"ym-sr\": \"耐药\"," +
//				"          \"ym-group\": \"A\"" +
//				"        } " +
//				"      ]" +
//				"    }" +
//
//				"	]" +
//				"}</FINAL1>" +
//				"  </Table>" +
//				"</ResultsDataSet>";


        System.out.println("resultXml:"+resultXml);

        Map<String, Object> resultMap = (Map<String, Object>) XmlToMap.xml2mapWithAttr("<BSXml>"+resultXml.toString()+"</BSXml>",false);

        //解析数据
        Object ResultsDataSet = resultMap.get("ResultsDataSet");
        if(ResultsDataSet instanceof Map){

        }else {
            warnBuff.append("未查询到微生物对应报告！");
            warnBuff.append("\n");
            return warnBuff.toString();
        }
        Map<String, Object> resultsDataSetMap = (Map<String, Object>)resultMap.get("ResultsDataSet");
        Object objectTable = resultsDataSetMap.get("Table");
        List<Map<String,Object>> mapList =  null;
        if(objectTable instanceof Map) {
            mapList =  new ArrayList<Map<String,Object>>();
            mapList.add((Map<String,Object>)objectTable);
        }else {
            mapList =  (ArrayList<Map<String,Object>>)objectTable;
        }

        /**
         * 此代码按照一个条码号返回一条数据处理
         */
        if(mapList != null && mapList.size() >0) {
            Map<String, Object> vPatientInfo = null;
            for(Map<String,Object> vMap : mapList) {
                //循环处理每条结果数据
                //如果返回的数据CLINICID 医院编码为空。 则忽略此数据
                Object clincidObj = vMap.get("CLINICID");
                if(clincidObj == null) {
                    warnBuff.append("微生物CLINICID为空，程序将过滤此条记录,BARCODE【"+vMap.get("BARCODE")+"】;");
                    continue;
                }
                //根据迪安传过来的医院条码号转成医院本地条码号
                Map<String, Object> barcodeMap = jbzyyDiAnMicrobialMapper.getBarcodeByDiAnClinicid(vMap);
                if(barcodeMap == null || barcodeMap.size() == 0 || barcodeMap.get("DOCTADVISENO") == null) {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】。该条码号在LIS库无记录;");
                    warnBuff.append("\n");
                    continue;
                }
                //把查询出本地LIS的条码号以及样本号put在参数Map中
                vMap.put("CLINICID", barcodeMap.get("DOCTADVISENO"));
                vMap.put("SAMPLENO", barcodeMap.get("SAMPLENO"));
                int countsPatient = jbzyyDiAnMicrobialMapper.getPatientInfoByNoCounts(vMap);
                if(countsPatient > 0) {
                    //该报告已审核。 return
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】。该结果已审核不进行任何处理;");
                    warnBuff.append("\n");
                    continue;
                }
                //根据迪安TESTCODE 找出对应的testID
                Map<String,Object> testIdMap = jbzyyDiAnMicrobialMapper.getTestIDByDiAn(vMap);
                if(testIdMap == null || testIdMap.size() == 0) {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安项目编码【"+vMap.get("TESTCODE")+"】,在LIS库中无对应！");
                    warnBuff.append("\n");
                    continue;
                }
                vMap.put("TESTID", testIdMap.get("TESTID"));//项目编号
                //根据迪安MIC_SAMPLETYPE 找出对应的sampletype
                Map<String,Object> sampleTyepMap = jbzyyDiAnMicrobialMapper.getSampleType(vMap);
                if(sampleTyepMap == null || sampleTyepMap.size() == 0) {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安样本类型【"+vMap.get("MIC_SAMPLETYPE")+"】,在LIS库中无对应！");
                    warnBuff.append("\n");
                    continue;
                }
                vMap.put("SAMPLETYPE", sampleTyepMap.get("SAMPLETYPE"));

                //TESTID、SAMPLENO 判断培养结果表是否有数据
                int plantCount = jbzyyDiAnMicrobialMapper.getPlantByNoCounts(vMap);
                //迪安的报告日期格式转换
                vMap.put("REPORTDATE", vMap.get("REPORTDATE").toString().split("\\+")[0].replaceAll("T", " "));
                String IFMASCULINE = vMap.get("IFMASCULINE") == null ? "" : vMap.get("IFMASCULINE").toString();
                if("阴性".equals(IFMASCULINE)) {//0 阴性 1 阳性 2 涂片 3 免疫检测
                    //结果为阴性说明无菌，则不需要写细菌结果以及药敏结果表
                    vMap.put("RESULTTYPE", "0");
                }else if("阳性".equals(IFMASCULINE)) {
                    vMap.put("RESULTTYPE", "1");
                }else {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】,返回数据IFMASCULINE【"+IFMASCULINE+"】字段无对应,请联系开发增加对应！");
                    warnBuff.append("\n");
                    continue;
                }
                //获取培养结果
                Object ofinal1 =vMap.get("FINAL1");
                JSONObject jsonObject = JSONObject.parseObject(ofinal1.toString());
                //微生物结果类型。类型不同，返回的数据格式不一致
                String type = jsonObject.get("type").toString();
                String oValue = jsonObject.get("value").toString();
                Map<String,Object> finalMap = parseFinal(type, oValue);
                if(finalMap != null && finalMap.size() != 0) { //有返回结果的节点
                    vPatientInfo = getPatientInfo(vMap);//此处查询是为了补齐入库参数
                    Object obj = finalMap.get("value");
                    //如果value 有N菌值，则L_PLANT_RESULT表就要保存N条记录
                    if("11".equals(type)) { //该模版
                        for(String str : (List<String>)obj) {
                            vMap.put("TESTRESULT", str);
                            operatePlantResult(plantCount, vMap,vPatientInfo);
                        }
                    }else if("20".equals(type)) {//支原体模版Mh、Uu 两种支原体类型鉴定及药敏
                        for(String str : (List<String>)obj) {
                            if(str.indexOf("阴性") != -1 || str.indexOf("阳性") != -1) {
                                if(str.indexOf("Uu") != -1) {
                                    vMap.put("TESTRESULT", "解脲支原体:"+str);
                                }else if(str.indexOf("Mh") != -1){
                                    vMap.put("TESTRESULT", "人型支原体:"+str);
                                }
                                operatePlantResult(plantCount, vMap,vPatientInfo);
                            }
                        }
                    }else if("99".equals(type)) {
                        vMap.put("TESTRESULT", vMap.get("TANKFARM").toString()+vMap.get("IFMASCULINE").toString()+obj);
                        operatePlantResult(plantCount, vMap,vPatientInfo);
                    }else {
                        vMap.put("TESTRESULT", vMap.get("TANKFARM").toString()+vMap.get("IFMASCULINE").toString());
                        operatePlantResult(plantCount, vMap,vPatientInfo);
                    }
                    //如果是阳性，则需要保存细菌表以及药敏表 l_bio_result l_anit_result
                    if("阳性".equals(IFMASCULINE)) {
                        int index = 0;
                        if("20".equals(type)) { //该类型只针对支原体的鉴定及药敏
                            //支原体模版比较特殊需要单独处理
                            for(String str : (List<String>)obj) {//写入细菌表
                                if(str.indexOf("阳性") != -1) { //找到阳性结果 BIONO 细菌ID
                                    //查询是否已有细菌结果bioid = #{BIOID} AND  sampleno = #{SAMPLENO}
                                    Map<String,Object> itemMap = (Map<String,Object>)finalMap.get(str);
                                    vMap.put("TESTCODE", itemMap.get("item"));
                                    testIdMap = jbzyyDiAnMicrobialMapper.getTestIDByDiAn(vMap);
                                    if(testIdMap == null || testIdMap.size() == 0) {
                                        warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安支原体【"+itemMap.get("item")+"】,在LIS库中无对应！");
                                        warnBuff.append("\n");
                                        throw new Exception(warnBuff.toString());
                                    }
                                    vMap.put("BIOID", testIdMap.get("TESTID"));
                                    int bioResultCount = jbzyyDiAnMicrobialMapper.getBioResultCount(vMap);
                                    vMap.put("BIONO", index);//第几个细菌
                                    Map<String,Object> countMap = (Map<String,Object>)finalMap.get(str.replaceAll("阳性", "")+"计数");
                                    vMap.put("BIO_QUANTITY", countMap.get("rslt"));//细菌数量
                                    operateBioResult(bioResultCount, vMap, vPatientInfo);
                                    index++;

                                    //写入药敏表ANTI
                                    //支原体Uu、Mh 对同一种抗生素的药敏结果是一致的。所以每种支原体存一份药敏结果
                                    for(String str1 : (List<String>)obj) {
                                        if(str1.indexOf("Uu") == -1 && str1.indexOf("Mh") == -1) {
                                            Map<String,Object> type20Map = (Map<String,Object>)finalMap.get(str1.replaceAll("阳性", ""));
                                            vMap.put("CHINESENAME", type20Map.get("item"));
                                            vMap.put("ym-name", type20Map.get("item"));
                                            Map<String,Object> antiMap = jbzyyDiAnMicrobialMapper.getAntiDic(vMap);
                                            if(antiMap == null || antiMap.size() == 0) {
                                                warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安抗生素【"+type20Map.get("item")+"】,在LIS库中无对应！");
                                                warnBuff.append("\n");
                                                throw new Exception(warnBuff.toString());
                                            }
                                            String ANTIID = antiMap.get("ANTIID").toString();
                                            vMap.put("ANTIID", ANTIID);
                                            vMap.put("TESTRESULT", type20Map.get("rslt"));
                                            int antiCount = jbzyyDiAnMicrobialMapper.getAntiResultCount(vMap);
                                            operateAntiResult(antiCount, vMap, antiMap);
                                        }
                                    }
                                }
                            }
                        }else if("15".equals(type) || "16".equals(type) || "17".equals(type) || "19".equals(type)) {
                            //其他模版 ，目前除了10、11、99这两个类型模版阳性没的药敏结果外其他都有
                            //类型18的模版不满足  40以上是属于院感的模版，院感不外送
                            if(obj instanceof List) {
                                for(String str : (List<String>)obj) {//写入细菌表
                                    Object OObj = finalMap.get(str);
                                    if(OObj != null) {
                                        //此处要对照出 BIOID 细菌ID
                                        //根据item ，找出对应的BIOID
                                        String item = finalMap.get(str+"_item").toString();
                                        item = item.split("\\(")[0];
                                        vMap.put("TESTCODE", item);
                                        testIdMap = jbzyyDiAnMicrobialMapper.getTestIDByDiAn(vMap);
                                        if(testIdMap == null || testIdMap.size() == 0) {
                                            warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安细菌【"+item+"】,在LIS库中无对应！");
                                            warnBuff.append("\n");
                                            throw new Exception(warnBuff.toString());
                                        }
                                        vMap.put("BIOID", testIdMap.get("TESTID"));
                                        int bioResultCount = jbzyyDiAnMicrobialMapper.getBioResultCount(vMap);
                                        vMap.put("BIONO", index);//第几个细菌
                                        vMap.put("BIO_QUANTITY", "");//细菌数量
                                        operateBioResult(bioResultCount, vMap, vPatientInfo);
                                        index++;
                                        //写入药敏表ANTI
                                        //存的药敏信息。 一个细菌要做N个抗生素药敏测试
                                        List<Map<String,Object>> typeList = (List<Map<String,Object>>)finalMap.get(str);
                                        for(Map<String,Object> typeMap : typeList) {
                                            vMap.put("CHINESENAME", typeMap.get("ym-name").toString().split("\\(")[0].split("\\：")[0]);
                                            Map<String,Object> antiMap = jbzyyDiAnMicrobialMapper.getAntiDic(vMap);
                                            if(antiMap == null || antiMap.size() == 0) {
                                                warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，迪安抗生素【"+typeMap.get("ym-name").toString().split("\\(")[0].split("\\：")[0]+"】,在LIS库中无对应！");
                                                warnBuff.append("\n");
                                                throw new Exception(warnBuff.toString());
                                            }
                                            String ANTIID = antiMap.get("ANTIID").toString();
                                            vMap.put("ANTIID", ANTIID);
                                            int antiCount = jbzyyDiAnMicrobialMapper.getAntiResultCount(vMap);
                                            //S 敏感 I中介 R耐药
                                            if(typeMap.get("ym-sr").toString().indexOf("敏感") != -1) {
                                                vMap.put("TESTRESULT", "S");
                                            }else if(typeMap.get("ym-sr").toString().indexOf("中介") != -1) {
                                                vMap.put("TESTRESULT", "I");
                                            }else if(typeMap.get("ym-sr").toString().indexOf("耐药") != -1) {
                                                vMap.put("TESTRESULT", "R");
                                            }else {
                                                vMap.put("TESTRESULT", typeMap.get("ym-sr"));
                                            }
                                            vMap.putAll(typeMap);
                                            operateAntiResult(antiCount, vMap, antiMap);
                                        }
                                    }
                                }
                            }else {
                                warnBuff.append("条码号【"+vMap.get("CLINICID")+"】,检测结果IFMASCULINE【"+IFMASCULINE+"】，模版类型【"+finalMap.get("type")+"】,无YM药敏节点！若类型为【10、11、99】忽略此提示");
                                warnBuff.append("\n");
                            }
                        }else {
                            warnBuff.append("条码号【"+vMap.get("CLINICID")+"】,模版类型【"+finalMap.get("type")+"】,当前系统无法满足联系开发新增模版解析");
                            warnBuff.append("\n");
                        }
                    }
                }
            }
        }
        System.out.println("test微生物："+warnBuff.toString());
        return warnBuff.toString();
    }

    /**
     * 保存培养结果表
     * @Title: operatePlantResult
     * @Description: TODO
     * @param plantCount
     * @param vMap void
     * @author caidao
     * @date 2020年4月12日下午3:56:32
     */
    public int operatePlantResult(int plantCount, Map<String,Object> vMap,Map<String, Object> vPatientInfo) {
        vMap.putAll(vPatientInfo);
        if(plantCount > 0) {
            //说明已有数据需要更新
            return jbzyyDiAnMicrobialMapper.updatePlantResult(vMap);
        }else {
            //新增
            return jbzyyDiAnMicrobialMapper.insertPlantResult(vMap);
        }
    }

    public int operateBioResult(int bioCount, Map<String,Object> vMap,Map<String, Object> vPatientInfo) {
        vMap.putAll(vPatientInfo);
        if(bioCount > 0) {
            //说明已有数据需要更新
            return jbzyyDiAnMicrobialMapper.updateBioResult(vMap);
        }else {
            //新增
            return jbzyyDiAnMicrobialMapper.insertBioResult(vMap);
        }
    }

    public int operateAntiResult(int antiCount, Map<String,Object> vMap,Map<String, Object> vPatientInfo) {
        vMap.putAll(vPatientInfo);
        if(antiCount > 0) {
            //说明已有数据需要更新
            return jbzyyDiAnMicrobialMapper.updateAntiResult(vMap);
        }else {
            //新增
            return jbzyyDiAnMicrobialMapper.insertAntiResult(vMap);
        }
    }

    public Map<String, Object> getPatientInfo( Map<String,Object> vMap){
        return jbzyyDiAnMicrobialMapper.getPatientInfo(vMap);
    }

    //解析微生物返回节点FINAL1
    /**
     *
     * 返回Map 说明
     * key = "type"  当前模版类型   必有
     * key = "value" 模版菌的结果 string/list<String> 必有
     * key = "当模版菌的结果值为list时，list<String>便是key"  药敏结果。list<Map<String,Object>>	 非必须。阳性时才有
     * @Title: parseFinal
     * @Description: TODO
     * @param type
     * @param value
     * @return Map<String,Object>
     * @author caidao
     * @date 2020年4月12日下午3:10:04
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String,Object> parseFinal(String type , String value) {
        List<JSONObject> obj = null;
        List<JSONObject> objYM = null;
        List<String> vList = null;
        List<Map<String,Object>> tempList = null;
        Map<String,Object> tempMap = null;
        Map<String,Object> nMap = new HashMap<String,Object>();
        switch (type) {
            case "10":
                /** 阴性 阳性 有菌 无菌
                 {
                 //结果类型代码
                 "type":"10",
                 //结果值
                 "value":["沙眼衣原体检测：阴性（参考值：阴性）　　　"]
                 }
                 */
                nMap.put("type", type);
                nMap.put("value", value.replace("[", "").replace("]", "").replace("\"", ""));
                break;
            case "11":
                /**阴性 阳性 有菌 无菌
                 * {
                 "type":"11",
                 "value":[
                 {
                 // 项目
                 "item":"真菌培养：",
                 // 结果
                 "rslt":"未培养出真菌"
                 },
                 {
                 "item":"一般细菌培养：",
                 "rslt":"培养无菌生长"
                 }
                 ]
                 }
                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    vList.add(jsonObject.getString("item")+jsonObject.getString("rslt"));
                }
                nMap.put("value", vList);
                break;
            case "15":
                /**
                 * {
                 //该类型是药敏仪器MIC法做出的结果，提供了三个样本条码数据，包含套餐和两个菌种格式
                 以下给列出属性解释
                 "type":"15",
                 "value":[{
                 //项目
                 "item":"表皮葡萄球菌",
                 //结果,可为空
                 "rslt":"",
                 //专家评语
                 "expert":"该菌对β内酰胺类/β-内酰胺酶抑制剂复合物、口服头孢类、注射头孢类包括ⅠⅡⅡ利福平不能单独用于抗菌治疗。呋喃妥因、洛美沙星是仅用于或首选治疗泌尿道感染的抗菌药物。对四环素敏感的菌株也被认为对多西环素和米诺环素敏感。然而，对四环素中介或耐药的某些菌株可以对多西环素或米诺环素或二者敏感。",
                 //药敏结果，为药敏数组
                 "ym":[{
                 //抗生素
                 "ym-name":"青霉素",
                 //结果数值
                 "ym-value":">0.25",
                 //结果单位
                 "ym-unit":"μg/ml",
                 //耐药性
                 "ym-sr":"耐药",
                 //分组
                 "ym-group":"A"
                 },{"ym-name":"苯唑西林",
                 "ym-value":"<=0.25",
                 "ym-unit":"μg/ml",
                 "ym-sr":"敏感",
                 "ym-group":"A"},{"ym-name":"左氧氟沙星","ym-value":"<=1","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"C"},{"ym-name":"环丙沙星","ym-value":"<=1","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"C"},{"ym-name":"洛美沙星","ym-value":"<=2","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"U"},{"ym-name":"莫西沙星","ym-value":"<=0.5","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"C"},{"ym-name":"加替沙星","ym-value":"<=0.5","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"O"},{"ym-name":"利福平","ym-value":"<=1","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"B"},{"ym-name":"呋喃妥因","ym-value":"<=32","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"U"},{"ym-name":"多西环素","ym-value":"<=4","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"B"},{"ym-name":"四环素","ym-value":"<=4","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"B"},{"ym-name":"阿奇霉素","ym-value":"<=2","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"A"},{"ym-name":"红霉素","ym-value":"<=0.5","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"A"},{"ym-name":"万古霉素","ym-value":"<=2","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"B"},{"ym-name":"利奈唑胺","ym-value":"<=4","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"B"},{"ym-name":"替考拉宁","ym-value":"<=8","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"Inv"},{"ym-name":"克林霉素","ym-value":"<=0.5","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"A"},{"ym-name":"庆大霉素","ym-value":">16","ym-unit":"μg/ml","ym-sr":"耐药","ym-group":"C"},{"ym-name":"复方新诺明","ym-value":"<=2/38","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"A"},{"ym-name":"阿米卡星","ym-value":"<=16","ym-unit":"μg/ml","ym-sr":"敏感","ym-group":"O"}]
                 },
                 {
                 //项目
                 "item":"血培养套餐-厌氧菌",
                 //结果，如果是细菌为计数
                 "rslt":"培养5天无厌氧菌生长",
                 "expert":""
                 }
                 ]
                 }
                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    tempList = new ArrayList<Map<String,Object>>();
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    vList.add(val);
                    nMap.put(val+"_item", jsonObject.getString("item"));
                    Object oym = jsonObject.get("ym");
                    if(oym != null) { //说明有药敏检测
                        objYM = (List<JSONObject>)JSONObject.parse(oym.toString());
                        for(JSONObject o : objYM) {
                            tempMap = new HashMap<String,Object>();
                            tempMap.put("ym-name", o.get("ym-name"));
                            tempMap.put("ym-value", o.get("ym-value"));
                            tempMap.put("ym-unit", o.get("ym-unit"));
                            tempMap.put("ym-sr", o.get("ym-sr"));
                            tempMap.put("ym-group", o.get("ym-group"));
                            tempList.add(tempMap);
                        }
                    }
                    nMap.put(val, tempList);
                }
                nMap.put("value", vList);
                break;
            case "16":
                /**
                 * {
                 "type":"16",
                 "value":[
                 {
                 "item":"金黄色葡萄球菌",
                 "rslt":"",
                 "expert":"",
                 "ym":[
                 {
                 "ym-name":"庆大霉素(Gentamicin)",
                 "ym-value":"≤0.5",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"青霉素(Penicillin)",
                 "ym-value":"0.12",
                 "ym-sr":"R"
                 },
                 {
                 "ym-name":"克林霉素(Clindamycin)",
                 "ym-value":"≤0.25",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"利福平(Rifampicin)(不能单独使用)",
                 "ym-value":"≤0.5",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"万古霉素(Vancomycin)",
                 "ym-value":"≤0.5",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"环丙沙星(Ciprofloxacin)",
                 "ym-value":"≥8",
                 "ym-sr":"R"
                 },
                 {
                 "ym-name":"苯唑西林(Oxacillin)",
                 "ym-value":"1",
                 "ym-sr":"R"
                 },
                 {
                 "ym-name":"莫西沙星(Moxifloxacin)",
                 "ym-value":"4",
                 "ym-sr":"R"
                 },
                 {
                 "ym-name":"红霉素(Erythromycin)",
                 "ym-value":"≤0.25",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"四环素(Tetracycline)",
                 "ym-value":"≤1",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"左氧氟沙星(Levofloxacin)",
                 "ym-value":"≥8",
                 "ym-sr":"R"
                 },
                 {
                 "ym-name":"复方新诺明(Trimethoprim\\Sulfamethoxazole)",
                 "ym-value":"≤10",
                 "ym-sr":"S"
                 },
                 {
                 "ym-name":"利奈唑胺(Linezolid)",
                 "ym-value":"1",
                 "ym-sr":"S"
                 }
                 ]
                 }
                 ]
                 }
                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    tempList = new ArrayList<Map<String,Object>>();
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    vList.add(val);
                    nMap.put(val+"_item", jsonObject.getString("item"));
                    Object oym = jsonObject.get("ym");
                    if(oym != null) { //说明有药敏检测
                        objYM = (List<JSONObject>)JSONObject.parse(oym.toString());
                        for(JSONObject o : objYM) {
                            tempMap = new HashMap<String,Object>();
                            tempMap.put("ym-name", o.get("ym-name"));
                            tempMap.put("ym-value", o.get("ym-value"));
                            tempMap.put("ym-sr", o.get("ym-sr"));
                            tempList.add(tempMap);
                        }
                    }
                    nMap.put(val, tempList);
                }
                nMap.put("value", vList);
                break;
            case "17":
                /**
                 * {
                 该类型是药敏KB法做出的结果
                 "type":"17",
                 "value":[{
                 项目
                 "item":"流感嗜血杆菌",
                 结果,可为空
                 "rslt":"计数：++",
                 "ym":[{
                 抗生素
                 "ym-name":"复方新诺明：",
                 耐药性S表示敏感，R表示耐药，I表示中性
                 "ym-sr":"S",
                 解释抑菌环
                 "ym-standard":"≥16"
                 },{"ym-name":"氨苄西林/舒巴坦：","ym-sr":"S","ym-standard":"≥23"},{"ym-name":"美罗培南：","ym-sr":"S","ym-standard":"≥29"},{"ym-name":"氯霉素：","ym-sr":"S","ym-standard":"≥41"},{"ym-name":"头孢他啶：","ym-sr":"S","ym-standard":"≥36"},{"ym-name":"四环素：","ym-sr":"S","ym-standard":"≥23"},{"ym-name":"氨苄西林：","ym-sr":"S","ym-standard":"≥22"},{"ym-name":"头孢噻肟：","ym-sr":"S","ym-standard":"≥40"},{"ym-name":"阿莫西林/克拉维酸：","ym-sr":"S","ym-standard":"≥24"},
                 {"ym-name":"利福平：","ym-sr":"S","ym-standard":"≥31"},{"ym-name":"左氧氟沙星：","ym-sr":"S","ym-standard":"≥41"},{"ym-name":"哌拉西林/他唑巴坦：","ym-sr":"S","ym-standard":"≥39"}
                 ]
                 }]
                 }
                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    tempList = new ArrayList<Map<String,Object>>();
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    vList.add(val);
                    nMap.put(val+"_item", jsonObject.getString("item"));
                    Object oym = jsonObject.get("ym");
                    if(oym != null) { //说明有药敏检测
                        objYM = (List<JSONObject>)JSONObject.parse(oym.toString());
                        for(JSONObject o : objYM) {
                            tempMap = new HashMap<String,Object>();
                            tempMap.put("ym-name", o.get("ym-name"));
                            tempMap.put("ym-standard", o.get("ym-standard"));
                            tempMap.put("ym-sr", o.get("ym-sr"));
                            tempList.add(tempMap);
                        }
                    }
                    nMap.put(val, tempList);
                }
                nMap.put("value", vList);
                break;
            case "18":
                /**
                 * {
                 "type":"18",
                 "value":[
                 {
                 "item":"产酸克雷伯菌",
                 "rslt":"计数：++",
                 "ym":[
                 {
                 "ym-name":"特治星：",
                 "ym-sr":"S≤4/4",
                 "ym-standard":"R≥128/4S≤16/4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"阿米卡星：",
                 "ym-sr":"S≤4",
                 "ym-standard":"R≥64S≤16",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"氨苄西林/舒巴坦：",
                 "ym-sr":"R≥64/32",
                 "ym-standard":"R≥32/16S≤8/4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"头孢噻肟：",
                 "ym-sr":"R≥8",
                 "ym-standard":"R≥4S≤1",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"头孢西丁：",
                 "ym-sr":"R≥32",
                 "ym-standard":"R≥32S≤8",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"庆大霉素",
                 "ym-sr":"R≥16",
                 "ym-standard":"R≥16S≤4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"美罗培南",
                 "ym-sr":"S≤1",
                 "ym-standard":"R≥4S≤1",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"头孢他啶：",
                 "ym-sr":"R16",
                 "ym-standard":"R≥16S≤4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"头孢吡肟：",
                 "ym-sr":"R≥16",
                 "ym-standard":"R≥32S≤8",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"亚胺培南：",
                 "ym-sr":"S≤1",
                 "ym-standard":"R≥4S≤1",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"头孢唑林：",
                 "ym-sr":"R≥64",
                 "ym-standard":"R≥8S≤2",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"氨曲南：",
                 "ym-sr":"I8",
                 "ym-standard":"R≥16S≤4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"氨苄西林：",
                 "ym-sr":"R≥32",
                 "ym-standard":"R≥32S≤8",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"阿莫西林/克拉维酸",
                 "ym-sr":"I32/2",
                 "ym-standard":"R≥32/16S≤8/4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"环丙沙星：",
                 "ym-sr":"R≥4",
                 "ym-standard":"R≥4S≤1",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"四环素：",
                 "ym-sr":"R≥16",
                 "ym-standard":"R≥16S≤4",
                 "ym-method":"MIC法"
                 },
                 {
                 "ym-name":"左氧氟沙星：",
                 "ym-sr":"R≥8",
                 "ym-standard":"R≥2S≤0.12",
                 "ym-method":"MIC法"
                 }
                 ]
                 }
                 ]
                 }
                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    tempList = new ArrayList<Map<String,Object>>();
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    vList.add(val);
                    nMap.put(val+"_item", jsonObject.getString("item"));
                    Object oym = jsonObject.get("ym");
                    if(oym != null) { //说明有药敏检测
                        objYM = (List<JSONObject>)JSONObject.parse(oym.toString());
                        for(JSONObject o : objYM) {
                            tempMap = new HashMap<String,Object>();
                            tempMap.put("ym-name", o.get("ym-name"));
                            tempMap.put("ym-standard", o.get("ym-standard"));
                            tempMap.put("ym-sr", o.get("ym-sr"));
                            tempMap.put("ym-method", o.get("ym-method"));
                            tempList.add(tempMap);
                        }
                    }
                    nMap.put(val, tempList);
                }
                nMap.put("value", vList);
                break;
            case "19":
                /**
                 * {
                 "type":"19",
                 "value":[
                 {
                 "item":"肺炎链球菌",
                 "rslt":"计数：+++",
                 "ym":[
                 {
                 "ym-name":"复方新诺明：",
                 "ym-value":"6",
                 "ym-sr":"R",
                 "ym-ref":"15-19",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"红霉素：",
                 "ym-value":"6",
                 "ym-sr":"R",
                 "ym-ref":"15-21",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"克林霉素：",
                 "ym-value":"6",
                 "ym-sr":"R",
                 "ym-ref":"15-19",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"万古霉素：",
                 "ym-value":"21",
                 "ym-sr":"S",
                 "ym-ref":"16-17",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"左氧氟沙星：",
                 "ym-value":"19",
                 "ym-sr":"S",
                 "ym-ref":"13-17",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"四环素：",
                 "ym-value":"10",
                 "ym-sr":"R",
                 "ym-ref":"18-23",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"氯霉素：",
                 "ym-value":"22",
                 "ym-sr":"S",
                 "ym-ref":"20-21",
                 "ym-unit":"mm"
                 },
                 {
                 "ym-name":"利福平：",
                 "ym-value":"25",
                 "ym-sr":"S",
                 "ym-ref":"16-19",
                 "ym-unit":"mm"
                 }
                 ]
                 }
                 ]
                 }

                 */
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                nMap.put("type", type);
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    tempList = new ArrayList<Map<String,Object>>();
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    vList.add(val);
                    nMap.put(val+"_item", jsonObject.getString("item"));
                    Object oym = jsonObject.get("ym");
                    if(oym != null) { //说明有药敏检测
                        objYM = (List<JSONObject>)JSONObject.parse(oym.toString());
                        for(JSONObject o : objYM) {
                            tempMap = new HashMap<String,Object>();
                            tempMap.put("ym-name", o.get("ym-name"));
                            tempMap.put("ym-value", o.get("ym-value"));
                            tempMap.put("ym-sr", o.get("ym-sr"));
                            tempMap.put("ym-ref", o.get("ym-ref"));
                            tempMap.put("ym-unit", o.get("ym-unit"));
                            tempList.add(tempMap);
                        }
                    }
                    nMap.put(val, tempList);
                }
                nMap.put("value", vList);
                break;
            case "20":
                /**
                 * {
                 "type":"20",
                 "value":[{
                 项目
                 "item":"Uu",
                 结果
                 "rslt":"阳性",
                 结果类型1-表示都是支原体要求的项目，2-表示套餐项目
                 "type":"1"
                 },
                 {"item":"Mh","rslt":"阴性","type":"1"},
                 {"item":"Uu计数","rslt":"＜10^4","type":"1"},
                 {"item":"Mh计数","rslt":"-","type":"1"},
                 {"item":"四环素","rslt":"S","type":"1"},
                 {"item":"左氧沙星","rslt":"I","type":"1"},
                 {"item":"红霉素","rslt":"I","type":"1"},
                 {"item":"交沙霉素","rslt":"S","type":"1"},
                 {"item":"强力霉素","rslt":"S","type":"1"},
                 {"item":"环丙沙星","rslt":"I","type":"1"},
                 {"item":"氧氟沙星","rslt":"I","type":"1"},
                 {"item":"美满霉素","rslt":"S","type":"1"},
                 {"item":"罗红霉素","rslt":"S","type":"1"},
                 {"item":"阿奇霉素","rslt":"S","type":"1"},
                 {"item":"克拉霉素","rslt":"S","type":"1"},
                 {"item":"司帕沙星","rslt":"I","type":"1"},
                 {"item":"快速沙眼衣原体检测：","rslt":"阴性（参考值：阴性）","type":"2"},
                 {"item":"淋球菌培养：","rslt":"未培养出淋球菌","type":"2"},
                 { 项目 "item":"涂片找淋球菌：",/*结果 "rslt":"涂片未找到革兰氏阴性双球菌",/*结果类型1-表示都是支原体要求的项目，2-表示套餐项目 "type":"2"}
                 ]
                 }
                 */
                nMap.put("type", type);
                obj = (List<JSONObject>)JSONObject.parse(value.toString());
                vList = new ArrayList<String>();
                for(JSONObject jsonObject : obj) {
                    String val = jsonObject.getString("item")+jsonObject.getString("rslt");
                    tempMap = new HashMap<String,Object>();
                    tempMap.put("item", jsonObject.get("item"));
                    tempMap.put("rslt", jsonObject.get("rslt"));
                    tempMap.put("type", jsonObject.get("type"));
                    if(jsonObject.getString("item").indexOf("计数") != -1) {
                        vList.add(jsonObject.getString("item"));
                        nMap.put(jsonObject.getString("item"), tempMap);
                    }else {
                        vList.add(val);
                        nMap.put(val, tempMap);
                    }
                }
                nMap.put("value", vList);
                break;
            /**
             *
             <FINAL1>{
             "type": "99",
             "value": [
             {
             "f1": "未检出霍乱弧菌,未检出沙门菌属及志贺菌属",
             "f50": "杭州无菌1抬头模版.frf",
             "pfn": "三线培养项目x"
             }
             ]
             }</FINAL1>
             */
            case "99":
                obj = (List)JSONObject.parse(value.toString());
//			JSONObject valueObject = JSONObject.parseObject(value.toString());
//			newValue = valueObject.get("f1").toString();
                nMap.put("type", type);
                JSONObject valueObject = (JSONObject)obj.get(0);
                nMap.put("value", valueObject.get("f1").toString());
                break;
            default:
                break;
        }
        return nMap;
    }
}
