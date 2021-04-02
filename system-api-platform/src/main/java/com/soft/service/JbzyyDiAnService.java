package com.soft.service;

import com.soft.api.mapper.CQ.JbzyyDiAnMapper;

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
 * 〈迪安一般检验结果描述〉<br>
 *
 * @className: jbzyyDiAnService
 * @package: com.soft.service
 * @author: Ljx
 * @date: 2021/03/19 15:24
 */
@Slf4j
@Service
public class JbzyyDiAnService {

    @Autowired
    private JbzyyDiAnMapper jbzyyDiAnMapper;

    @SuppressWarnings({"unchecked" })
    @Transactional(rollbackFor=Exception.class)
    public String parseAndSaveDiAnResult(String... args) throws Exception{
        StringBuffer warnBuff = new StringBuffer();
        String url = args[0];
        String ClientID = args[1];
        String ClientGUID = args[2];
        String method = args[3];
        String StartDate= DateTimeFormatterUtil.format(DateTimeFormatterUtil.getNextDay(new Date(), -1), "yyyy-MM-dd");
        String EndDate = DateTimeFormatterUtil.format(LocalDateTime.now(), "yyyy-MM-dd");

//		Object resultXml = CallInterfaceUtil.call(url, method, PARAMS);
        String[] params = new String[] {ClientID,ClientGUID,StartDate,EndDate};
        List<String> parameter = new ArrayList<String>();
        parameter.add("ClientID");parameter.add("ClientGUID");parameter.add("StartDate");parameter.add("EndDate");
        log.info("调用webServiceAsmx参数【"+ClientID+";"+ClientGUID+";"+StartDate+";"+EndDate+"】");
        Object resultXml = CallInterfaceUtil.webServiceAsmx(url, "http://report.dagene.net/", params, method, parameter);
        if(resultXml == null || resultXml == "" || resultXml == "null") {
            warnBuff.append("调用method【"+method+"】,无数据！入参【"+params+"】");
        }
        System.out.println("resultXml:"+resultXml);

        Map<String, Object> resultMap = (Map<String, Object>) XmlToMap.xml2mapWithAttr("<BSXml>"+resultXml.toString()+"</BSXml>",false);
        //解析数据
        Map<String, Object> resultsDataSetMap = (Map<String, Object>)resultMap.get("ResultsDataSet");
        List<Map<String,Object>> mapList =  (ArrayList<Map<String,Object>>)resultsDataSetMap.get("Table");
        Map<String,List<Map<String, Object>>> classMap = new HashMap<String,List<Map<String, Object>>>();
        if(mapList != null && mapList.size() >0) {
            //按迪安接口数据，根据CLINICID医院条码号字段将数据分类
            //map (key barcode , value list)
            for(Map<String,Object> m : mapList) {
                Object clincidObj = m.get("CLINICID");
                if(clincidObj == null) {
                    if(warnBuff.indexOf(m.get("BARCODE").toString()) == -1) {
                        warnBuff.append("分组CLINICID为空，程序将过滤此条记录,BARCODE【"+m.get("BARCODE")+"】;");
                    }
                    continue;
                }
                String clincid = clincidObj.toString();
                List<Map<String, Object>> ltemp = classMap.get(clincid);
                if(ltemp == null) {
                    ltemp = new ArrayList<Map<String,Object>>();
                }
                packParam(m);
                ltemp.add(m);
                classMap.put(clincid, ltemp);
            }
            warnBuff.append("一般检验结果将条码号分组完成：分组条码号为【"+classMap.keySet()+"】;");
            for (Map.Entry<String, List<Map<String, Object>>> m : classMap.entrySet()) {
                //System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                //同一个list集合，Barcode是一样的
                List<Map<String, Object>> vList = m.getValue();
                Map<String, Object> vMap = vList.get(0);
                warnBuff.append("处理条码号为：【"+vMap.get("CLINICID")+"】数据，数据条数：【"+vList.size()+"】;");

                Map<String, Object> barcodeMap = jbzyyDiAnMapper.getBarcodeByDiAnClinicid(vMap);
                if(barcodeMap == null || barcodeMap.size() == 0 || barcodeMap.get("DOCTADVISENO") == null) {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】。该条码号在LIS库无记录;");
                    warnBuff.append("\n");
                    continue;
                }
                vMap.put("CLINICID", barcodeMap.get("DOCTADVISENO"));
                vMap.put("SAMPLENO", barcodeMap.get("SAMPLENO"));
                int countsPatient = jbzyyDiAnMapper.getPatientInfoByNoCounts(vMap);
                if(countsPatient > 0) {
                    //该报告已审核。 return
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】。该结果已审核不进行任何处理;");
                    warnBuff.append("\n");
                    continue;
                }
                Map<String, Object> patientPara  = jbzyyDiAnMapper.getPatientInfoMap(vMap);
                if(patientPara == null) {
                    warnBuff.append("条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】。没有查询到添加的报告信息;");
                    warnBuff.append("\n");
                    continue;
                }
                //如果没有样本号，则根据生成的样本号 进行更新
                if( barcodeMap.get("ISSAMPLENO") == null){
                    jbzyyDiAnMapper.updateJytmxx(vMap);
                }
                //从这行代码开始操作表的增、删、改、
                //删除
                jbzyyDiAnMapper.deletePatientInfoByNo(vMap);
                //医院条码号
                patientPara.put("APPRVEDBY", vMap.get("APPRVEDBY"));
                patientPara.put("SAMPLENO", barcodeMap.get("SAMPLENO"));
                patientPara.put("CHECKTIME", vMap.get("APPRDATE").toString().split("\\+")[0].replaceAll("T", " "));
                jbzyyDiAnMapper.insertPatientInfo(patientPara);
                //将迪安项目编号转换成本地系统
                List<Object> testidList = new ArrayList<>();
                boolean bool = true;
                for(Map<String, Object> vm : vList) {
                    Map<String, Object> oMap = jbzyyDiAnMapper.getTestIDByDiAnS(vm);
                    if(oMap == null || oMap.size() == 0) {
                        warnBuff.append("迪安项目编码转换失败！条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，项目编号【"+vm.get("S")+"】");
                        warnBuff.append("\n");
                        bool = false;
                        break;
                        //throw new Exception("迪安项目编码转换失败，此次操作全部回滚！【"+ClientID+";"+ClientGUID+";"+StartDate+";"+EndDate+"】");
                    }else {
                        vm.put("TESTID", oMap.get("TESTID"));
                        testidList.add(oMap.get("TESTID"));
                    }
                    Map<String, Object> oSampleType =jbzyyDiAnMapper.getSampleType(vm);
                    if(oSampleType == null || oSampleType.size() == 0) {
                        warnBuff.append("迪安样本类型转换失败！条码号【"+vMap.get("CLINICID")+"】，迪安条码号【"+vMap.get("BARCODE")+"】，样本类型【"+vm.get("SAMPLETYPE")+"】");
                        warnBuff.append("\n");
                        bool = false;
                        break;
                        //throw new Exception("迪安样本类型转换失败，此次操作全部回滚！【"+ClientID+";"+ClientGUID+";"+StartDate+";"+EndDate+"】");
                    }else {
                        vm.put("SAMPLETYPE", oSampleType.get("SAMPLETYPE"));
                    }
                    vm.put("SUBMITDATE", vm.get("SUBMITDATE").toString().split("\\+")[0].replaceAll("T", " "));
                    vm.put("RANGE_FLG", "N".equals(vm.get("RANGE_FLG").toString()) ? 0 : 1); //0为危急值
                    vm.put("SAMPLENO", barcodeMap.get("SAMPLENO"));
                }
                //删除L_TESTRESULT 表中数据
                //bool 是判断条码转换、样本类型转换。一个条码号下面需要全部转换成功，才能执行以下代码
                if(bool == true) {
                    Map<String, Object> tempM = new HashMap<String, Object>();
                    tempM.put("SAMPLENO", barcodeMap.get("SAMPLENO"));
                    tempM.put("TESTID", testidList);
                    jbzyyDiAnMapper.deleteTestResult(tempM);
                    System.out.println("vlist"+vList.toString());
                    jbzyyDiAnMapper.insertL_TestResult(vList);
                    jbzyyDiAnMapper.updatePatientInfo(patientPara);
                }
            }
        }
        System.out.println("tset:"+warnBuff.toString());
        return warnBuff.toString();
    }

    public void packParam(Map<String,Object> m) {
        m.put("RESULTFLAG", "AAAAAA");
        m.put("TESTSTATUS", "4");//LIS罗凯说的默认4
        m.put("LABDEPARTMENT", "3082"); //3082 外检
        m.put("ISPRINT", "1");
        m.put("MEASURENUM", "1");
        m.put("CORRECTFLAG", "1");
        m.put("DEVICEID", "");
        m.put("FEE", "0");
        m.put("ARCHIVESTATUS", "0");
        m.put("EDITSTATUS", "0");
        m.put("OPERATOR", 863);//操作员ID
    }
}
