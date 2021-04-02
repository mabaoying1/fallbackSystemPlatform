package com.soft.api.service.lis.shengguke;

import com.soft.api.mapper.shengguke.IInspectionRelationMapper;
import ctd.net.rpc.util.DateUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 检验报告审核通知/检验报告取消审核通知
 * @ClassName LabReportAuditORCancelImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/12
 * @Version V1.0
 **/
@Service
public class LabReportAuditORCancelImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    /**
     * 检验报告审核通知
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labReportAudit(Map<String,Object> map) throws Exception {
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        labBarcodeMap.put("BarcodeNo", "DSF"+labBarcodeMap.get("BarcodeNo").toString()+"A");
        String auditDateTime = (String) labBarcodeMap.get("AuditDateTime");
        map.put("AUDITDATETIME", auditDateTime.replaceAll("T", " "));
        int patientRw = inspectionRelationMapper.getLPatientInfo(labBarcodeMap);
//        int testRw = inspectionRelationMapper.getLTestResult(map);
        if(patientRw == 0){
            return ResultMessage.error(500,"检验报告审核通知条码号未查询到");
        }
        Map<String,Object> auditDoctorMap = (Map<String,Object>)labBarcodeMap.get("AuditDoctor");
        labBarcodeMap.put("AuditDoctor", auditDoctorMap.get("#text"));
        labBarcodeMap.put("AuditDateTime", labBarcodeMap.get("AuditDateTime").toString().replaceAll("T"," "));
        int rows = inspectionRelationMapper.updateLpatientinfoAudit(labBarcodeMap);
        if(rows < 1){
            throw new Exception("检验报告审核通知更新【l_patientinfo】失败");
        }
        rows = inspectionRelationMapper.updateLtestresultAudit(labBarcodeMap);
//        if(rows < 1){
//            throw new Exception("检验报告审核通知更新【l_testresult】失败");
//        }
        return ResultMessage.success(200,"检验报告审核通知成功");
    }


    /**
     * 取消检验报告审核通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labReportAuditCancel(Map<String,Object> map)throws Exception{
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        labBarcodeMap.put("BarcodeNo","DSF"+labBarcodeMap.get("BarcodeNo").toString()+"A");
        int patientRw = inspectionRelationMapper.getLPatientInfo(labBarcodeMap);
        if(patientRw == 0){
            return ResultMessage.error(500,"取消检验报告审核通知条码号未查询到");
        }
        int rows = inspectionRelationMapper.updateLpatientinfoCancel(labBarcodeMap);
        if(rows < 1){
            throw new Exception("检验报告审核通知更新【l_patientinfo】失败");
        }
        rows = inspectionRelationMapper.updateLtestresultCancel(labBarcodeMap);
//        if(rows < 1){
//            throw new Exception("检验报告审核通知更新【l_testresult】失败");
//        }
        return ResultMessage.success(200,"取消检验报告审核通知成功");
    }
}
