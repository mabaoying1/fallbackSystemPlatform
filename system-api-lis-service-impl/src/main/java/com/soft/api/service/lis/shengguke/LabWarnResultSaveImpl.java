package com.soft.api.service.lis.shengguke;

import ctd.net.rpc.util.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 检验危急值发布(回写LIS)
 * @ClassName LabWarnResultSaveImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/17
 * @Version V1.0
 **/
public class LabWarnResultSaveImpl {

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labWarnResultSave(Map<String,Object> map){
        Map paramMap = packParam(map);
        /**
         * insert into L_WARNTESTBLOG (
         *  SAMPLENO, TESTID , PATIENTNAME , SECTION,
         *  BEDNO, RESULT, PATIENTID,
         *  ROUND, REPORTOR, RECEIVER,
         *  REPORTTIME, BZ, RECEIVETIME,TSNR,
         *  LABDEPARTMENT,
         * )values(
         *  #{SampleNo} , #{TestID} , #{Name} , #{DeptCode},
         *  #{SickbedId}, #{RESULT,jdbcType=VARCHAR} , #{SourcePatientId},
         *  #{ROUND,jdbcType=VARCHAR}, #{ReportDoctor} , #{NoticeDoctor},
         *  #{ReportTime},#{BZ,jdbcType=VARCHAR}, SYSDATE,#{RemarkInfo},
         *  #{ReportDept}
         * )
         *
         */
        return null;
    }

    public Map packParam(Map<String,Object> map){
        Map patientMap = (Map)map.get("Patient");
        Map labWarnRecordMap = (Map)map.get("LabWarnRecord");
        Map visitMap = (Map)map.get("Visit");
        Map paramMap = new HashMap();
        //L_WARNTESTBLOG
        paramMap.put("SampleNo" , labWarnRecordMap.get("SampleNo"));
        paramMap.put("TestID" , labWarnRecordMap.get("TestID"));
        paramMap.put("Name" , patientMap.get("Name"));
        if(visitMap.get("DeptCode") instanceof Map){ //获取患者科室
            Map deptMap = (Map)visitMap.get("DeptCode");
            paramMap.put("DeptCode" , deptMap.get("#text"));
        }else{
            paramMap.put("DeptCode" , visitMap.get("DeptCode"));
        }
        paramMap.put("SickbedId" , labWarnRecordMap.get("SickbedId"));
        paramMap.put("SourcePatientId", patientMap.get("SourcePatientId"));
        if(labWarnRecordMap.get("ReportDoctor") instanceof Map){ //获取发布危机值医生
            Map reportDoctorMap = (Map)labWarnRecordMap.get("ReportDoctor");
            paramMap.put("ReportDoctor" , reportDoctorMap.get("#text"));
        }else{
            paramMap.put("ReportDoctor" , labWarnRecordMap.get("DeptCode"));
        }
        if(labWarnRecordMap.get("NoticeDoctor") instanceof Map){ //获取接收危机值医生
            Map noticeDoctorMap = (Map)labWarnRecordMap.get("NoticeDoctor");
            paramMap.put("NoticeDoctor" , noticeDoctorMap.get("#text"));
        }else{
            paramMap.put("NoticeDoctor" , labWarnRecordMap.get("NoticeDoctor"));
        }
        paramMap.put("ReportTime" , labWarnRecordMap.get("ReportTime").toString().replaceAll("T", " "));
        paramMap.put("RemarkInfo", labWarnRecordMap.get("RemarkInfo"));
        if(labWarnRecordMap.get("ReportDept") instanceof Map){ //获取发布危机值医生
            Map reportDeptMap = (Map)labWarnRecordMap.get("ReportDept");
            paramMap.put("ReportDept" , reportDeptMap.get("#text"));
        }else{
            paramMap.put("ReportDept" , labWarnRecordMap.get("ReportDept"));
        }
        return paramMap;
    }

}
