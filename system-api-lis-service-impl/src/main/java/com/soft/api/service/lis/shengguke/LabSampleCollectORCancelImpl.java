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
 * 检验标本采集通知/取消检验标本采集通知
 * @ClassName labSampleCollectORCancel
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/12
 * @Version V1.0
 **/
@Service
public class LabSampleCollectORCancelImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    @Autowired
    private QueryLISKeyCommon queryLISKeyCommon;

    /**
     * 检验标本采集通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleCollect(Map<String,Object> map) throws Exception {
        long dqz = queryLISKeyCommon.getMaxANDValide("L_JYTMXX_BBZQ",1);
        // 采集通知业务代码
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        labBarcodeMap.put("XH", dqz);
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String)labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        labBarcodeMap.put("BarcodeNo",sb.toString());
        String collectDateTime = (String) labBarcodeMap.get("CollectDateTime");
        labBarcodeMap.put("CollectDateTime",  DateUtil.strToDateFormat(collectDateTime, "T"));
        Map<String,Object> collectOperatorMap = (Map<String,Object>)labBarcodeMap.get("CollectOperator");
        labBarcodeMap.put("CollectOperator", collectOperatorMap.get("#text"));
        int rows = inspectionRelationMapper.updateLJytmxxCollect(labBarcodeMap);
        if(rows < 1){
            throw new Exception("更新【L_JYTMXX】EXECUTETIME、EXECUTOR失败");
        }
        rows = inspectionRelationMapper.insertLJytmxxBbzqCollect(labBarcodeMap);
        if(rows < 1){
            throw new Exception("新增【L_JYTMXX_BBZQ】失败");
        }
        return ResultMessage.success(200,"检验标本采集通知成功");
    }

    /**
     * 取消检验标本采集通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleCollectCancel(Map<String,Object> map){
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String) labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        map.put("BARCODENO", sb.toString());
        int updateRows = inspectionRelationMapper.updateLjytmxxCollectCancel(map);
        if(updateRows < 1){
            return ResultMessage.error(500,"取消检验标本采集通知失败【L_JYTMXX】,更新条数【0】");
        }
        return ResultMessage.success(200,"取消检验标本采集通知成功");
    }

}
