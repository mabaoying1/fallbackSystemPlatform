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
 * 检验标本核收通知/检验标本退回通知
 * @ClassName LabSampleReceiveORReturnImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/12
 * @Version V1.0
 **/
@Service
public class LabSampleReceiveORReturnImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    @Autowired
    private QueryLISKeyCommon queryLISKeyCommon;


    /**
     * 检验标本接收通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleReceive(Map<String,Object> map) throws Exception {
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        // 获取标序号
        long dqz = queryLISKeyCommon.getMaxANDValide("L_JYTMXX_BBZQ",1);
        map.put("XH", dqz);
        // 采集通知业务代码
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String)labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        map.put("BarcodeNo", sb.toString());
        String receiveDateTime = (String) labBarcodeMap.get("ReceiveDateTime");
        map.put("ReceiveDateTime", DateUtil.strToDateFormat(receiveDateTime, "T"));
        Map<String,Object> receiveOperatorMap = (Map<String,Object>)labBarcodeMap.get("ReceiveOperator");
        map.put("ReceiveOperator", receiveOperatorMap.get("#text"));
        map.put("SampleNo", labBarcodeMap.get("SampleNo"));
        int rows = inspectionRelationMapper.updateLJYTMXX(map);
        if(rows < 1){
            throw new Exception("标本接收通知更新【l_jytmxx】失败");
        }
        rows = inspectionRelationMapper.insertLJYTMXXBBZQ(map);
        if(rows < 1){
            throw new Exception("标本接收通知新增【L_JYTMXX_BBZQ】失败");
        }
        return ResultMessage.success(200,"检验标本接收通知成功");
    }

    /**
     * 检验标本退回通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity labSampleReturn(Map<String,Object> map){
        Map<String,Object> labBarcodeMap = (Map<String,Object>)map.get("LabBarcode");
        StringBuffer sb = new StringBuffer("DSF");
        sb.append((String) labBarcodeMap.get("BarcodeNo"));
        sb.append("A");
        map.put("BARCODENO", sb.toString());
        Map<String,Object> operatorMap = (Map<String,Object>)labBarcodeMap.get("Operator");
        map.put("OPERATOR", operatorMap.get("#text"));
        int updateRows = inspectionRelationMapper.updateLjytmxx(map);
        if(updateRows < 1){
            return ResultMessage.error(500,"检验标本退回通知更新【l_jytmxx】失败");
        }
        return ResultMessage.success(200,"检验标本退回通知成功");
    }

}
