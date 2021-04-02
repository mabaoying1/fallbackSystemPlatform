package com.soft.api.service.lis.shengguke;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.feign.HISServiceFeign;
import com.soft.api.mapper.shengguke.IInspectionRelationMapper;
import ctd.net.rpc.util.LabBarcodeUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检验计费取消
 * @ClassName LabRequestInHospitalReturnBillImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/29
 * @Version V1.0
 **/

@Service
public class LabRequestInHospitalReturnBillImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    @Autowired
    private HISServiceFeign hisServiceFeign;

    @Autowired
    private LabBarcodeUtil labBarcodeUtil;


    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction( propagation = DTXPropagation.REQUIRED)
    public ResponseEntity labRequestInHospitalReturnBill(Map<String,Object> map) throws Exception {
        StringBuffer doctadviseno = new StringBuffer("DSF");
        Map barCodeMap = (Map)map.get("LabBarcode");
        doctadviseno.append((String)barCodeMap.get("BarcodeNo"));
        doctadviseno.append("A");
        map.put("doctadviseno", doctadviseno.toString());
        map.put("DOCTADVISENO", doctadviseno.toString());

        //查询是否条码号存在
        Map stayhospitalmodeMap = inspectionRelationMapper.isJytmxx(map);
        if(stayhospitalmodeMap == null || stayhospitalmodeMap.size() == 0 ){
            throw new Exception("取消检验计费失败,根据条码号未查询到申请单ID,入参【"+map+"】");
        }
        // 门诊还是住院
        if("1".equals(stayhospitalmodeMap.get("STAYHOSPITALMODE").toString())
                || "3".equals(stayhospitalmodeMap.get("STAYHOSPITALMODE").toString())){
            return ResultMessage.success(200,"【labRequestInHospitalReturnBill】只用于住院检验退费,不能适用于门诊");
        }
        ResponseEntity responseEntity = hisServiceFeign.queryCYPB(JSON.toJSONString(stayhospitalmodeMap));
        if(responseEntity.getCode() == 400){
            throw new Exception("HIS访问超时，服务熔断【queryCYPB】;此次访问失败");
        }
        if(Integer.parseInt(responseEntity.getResult().toString()) > 0){
            return ResultMessage.error(500,"取消检验条码打印通知失败,已经出院无法取消条码打印");
        }
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) inspectionRelationMapper.queryIsRefundData(map);
        if(listMap != null && listMap.size() > 0){
            responseEntity = hisServiceFeign.cancelLISPay(JSON.toJSONString(listMap));
            if(responseEntity.getCode() == 400){
                throw new Exception("调用HIS接口退费失败;操作回滚");
            }
            if(responseEntity.getCode() == 500){
                throw new Exception(responseEntity.getMsg());
            }
        }else{
            throw new Exception("未查询出已计费数据,无法进行退费处理");
        }

        Map<String,Object> temp = new HashMap();
        for (Map<String,Object> request: listMap) {
            temp.put(request.get("REQUESTID").toString() , request.get("REQUESTID"));
            int rowsCancelBarCodeBillMark  = inspectionRelationMapper.cancelBarCodeBillingMark(request);
            //因为按条码退费，所以不能判断申请单更新状态条数
            if(rowsCancelBarCodeBillMark < 1)
                throw new Exception("取消申请单住院计费标志【l_lis_sqdmx】失败！");
        }
        int updateRows2 = inspectionRelationMapper.updateBarCodeBillingMarkCancel(map);
        if(updateRows2 < 1) //|| updateRows1 <1
            throw new Exception("取消申请单住院计费标志【l_jytmxx_mx】失败！");

        for(String key : temp.keySet()){
            Map tp = new HashMap();
            tp.put("REQUESTID",key);
            int rel = inspectionRelationMapper.getSQDMXZXPBCount(tp);
            if(rel == 0){   //更新
                int updateRows1 = inspectionRelationMapper.updateSqdstatusCance(tp);
                if(updateRows1 < 1){
                    throw new Exception("更新【l_lis_sqd】失败,更新数为0");
                }
            }
        }

//        Map<String,Object> map1 = new HashMap<>();
//        // lis库 sql 查询是否有数据可取消计费
//        Map<String, Object> mzZyCodeMap = inspectionRelationMapper.queryCancelMzZyCode(map);
//        if(mzZyCodeMap == null || mzZyCodeMap.size() == 0){
//            return ResultMessage.error(500,"取消检验计费失败,未查询到数据【"+map+"】");
//        }
//        Integer mzZyCode = Integer.parseInt(mzZyCodeMap.get("MZZYCODE").toString());
//        if(mzZyCode.intValue() == 1 || mzZyCode.intValue() == 3){
//            return ResultMessage.success(200,"【labRequestInHospitalReturnBill】只用于住院检验退费,不能适用于门诊");
//        }
//        ResponseEntity responseEntity = hisServiceFeign.queryCYPB(JSON.toJSONString(mzZyCodeMap));
//        if(responseEntity.getCode() == 400){
//            throw new Exception("HIS访问超时，服务熔断【queryCYPB】;此次访问失败");
//        }
//        if(Integer.parseInt(responseEntity.getResult().toString()) > 0){
//            return ResultMessage.error(500,"取消检验条码打印通知失败,已经出院无法取消条码打印");
//        }
//        List<Map<String, Object>> listMap = (List<Map<String, Object>>) inspectionRelationMapper.queryIsRefundData(map);
//        List<HashMap<String, Object>> requestList = labBarcodeUtil.getRequstid(listMap);
//        //调用取消检验计费接口
//        if(listMap != null && listMap.size() > 0){
//            responseEntity = hisServiceFeign.cancelLISPay(JSON.toJSONString(listMap));
//            if(responseEntity.getCode() == 400){
//                throw new Exception("调用HIS接口退费失败;操作回滚");
//            }
//            if(responseEntity.getCode() == 500){
//                throw new Exception(responseEntity.getMsg());
//            }
//        }else{
//            throw new Exception("未查询出已计费数据,无法进行退费处理");
//        }
//        // 取消申请单计费标志
//        for (HashMap<String,Object> request: requestList) {
//            String requestid = (String)request.get("REQUESTID");
//            request.put("RequestId", requestid);
//            request.put("FYXHS",labBarcodeUtil.getFyxh(listMap,requestid));
//            request.put("doctadviseno", doctadviseno.toString());
//            List<Map> prehyidMap = inspectionRelationMapper.getLjytmxxPrehyid(request);
//            for(Map m : prehyidMap){
//                request.put("PREHYID", m.get("PREHYID"));
//                int rowsCancelBarCodeBillMark  = inspectionRelationMapper.cancelBarCodeBillingMark(request);
//                //因为按条码退费，所以不能判断申请单更新状态条数
//                if(rowsCancelBarCodeBillMark < 1) //|| updateRows1 <1
//                    throw new Exception("取消申请单住院计费标志【l_lis_sqdmx】失败！");
//            }
//            int updateRows1 = inspectionRelationMapper.updateSqdstatusCance(request);
//            int updateRows2 = inspectionRelationMapper.updateBarCodeBillingMarkCancel(request);
//            if(updateRows2 < 1) //|| updateRows1 <1
//                throw new Exception("取消申请单住院计费标志【l_jytmxx_mx】失败！");
//        }
        return ResultMessage.success(200,"取消检验住院计费取消通知成功");
    }
}
