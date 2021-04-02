package com.soft.api.service.lis.shengguke;

import com.soft.api.mapper.shengguke.IInspectionRelationMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 检验标本送检通知/取消检验标本送检通知
 * @ClassName LabSampleSendORCancelImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/12
 * @Version V1.0
 **/
@Service
public class LabSampleSendORCancelImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    /**
     * 检验标本送检通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleSend(Map<String,Object> map){
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String )labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        map.put("DOCTADVISENO", sb.toString());
        int rows = inspectionRelationMapper.updateLabSampleSend(map);
        if(rows < 1){
            return ResultMessage.error(500,"检验标本送检通知失败,更新【L_JYTMXX】samplestatus条数【0】");
        }
        return ResultMessage.success(200,"检验标本送检通知成功");
    }

    /**
     * 取消检验标本送检通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleSendCancel(Map<String,Object> map){
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String )labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        map.put("DOCTADVISENO", sb.toString());
        int rows = inspectionRelationMapper.updateLabSampleSendCancel(map);
        if(rows < 1){
            return ResultMessage.error(500,"取消检验标本送检通知失败,更新【L_JYTMXX】samplestatus条数【0】");
        }
        return ResultMessage.success(200,"取消检验标本送检通知成功");
    }

}
