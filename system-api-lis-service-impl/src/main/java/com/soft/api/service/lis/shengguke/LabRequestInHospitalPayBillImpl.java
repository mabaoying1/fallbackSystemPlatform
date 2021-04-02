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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 住院计费 <br>
 * 1:查询条码是否存在    <br>
 * 2:判断门诊or住院<br>
 * 3:调用HIS服务计费<br>
 * 4:更新SQDMX<br>
 * 5:更新JYTMXX_MX<br>
 * @ClassName LabRequestInHospitalPayBillImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/29
 * @Version V1.0
 **/
@Service
public class LabRequestInHospitalPayBillImpl {

    @Autowired
    private HISServiceFeign hisServiceFeign;

    @Autowired
    private LabBarcodeUtil labBarcodeUtil;

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    /**
     * 检验住院计费方法
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    public ResponseEntity labRequestInHospitalPayBill(Map<String,Object> map) throws Exception {
        Map labBarcode = (Map) map.get("LabBarcode");
        Object labRequest  =  labBarcode.get("LabRequest");
        List<Map<String,Object>> requestList = new ArrayList<Map<String,Object>>();
        String gh = getDoctor(map); //员工工号
        Map<String,Object> paramMap = new HashMap<>();
        if(labRequest != null) { //说明接口入参是条码打印通知
            if (labRequest instanceof  Map){
                Map<String,Object> newMap = (Map<String,Object>) labRequest;
                requestList.add(newMap);
            }else if (labRequest instanceof List){
                requestList = (ArrayList<Map<String,Object>>)labRequest;
            }
            labBarcode.put("LabRequest",requestList);
            map.put("LabBarcode",labBarcode);
            Integer mzZyCode = inspectionRelationMapper.queryMzZyCode(map);
            if(mzZyCode == null){
                return ResultMessage.error(500,"住院计费失败,未查询到数据【"+map+"】");
            }
            if(mzZyCode.intValue() == 1 || mzZyCode.intValue() == 3){
                return ResultMessage.success(200,"【labRequestInHospitalPayBill】只用于住院检验计费,不能适用于门诊");
            }
            for (Map<String,Object> request : requestList){
                String requestId = (String)request.get("RequestId");
                request.put("ITEMCODE_TEXT", labBarcodeUtil.getItemCode(request));
                request.put("RequestId", requestId);
                request.put("REQUESTID", requestId);
            }
        }else{ //以下 标本采集或标本送检或标本接收
            Object oBarcodeNo = labBarcode.get("BarcodeNo");
            if(oBarcodeNo == null ){
                throw new Exception("检验住院计费进入(非条码打印计费)方法,未获取到【BarcodeNo】,入参【"+map+"】");
            }
            paramMap.put("doctadviseno", "DSF"+oBarcodeNo+"A") ;
            //查询是否条码号存在
            Map stayhospitalmodeMap = inspectionRelationMapper.isJytmxx(paramMap);
            if(stayhospitalmodeMap == null || stayhospitalmodeMap.size() == 0 ){
                throw new Exception("检验住院计费进入(非条码打印计费)方法,根据条码号未查询到申请单ID,入参【"+map+"】");
            }
            // 门诊还是住院
            if("1".equals(stayhospitalmodeMap.get("STAYHOSPITALMODE").toString())
                || "3".equals(stayhospitalmodeMap.get("STAYHOSPITALMODE").toString())){
                return ResultMessage.success(200,"【labRequestInHospitalPayBill】只用于住院检验计费,不能适用于门诊");
            }
//            List<Map> sqdIDMap = inspectionRelationMapper.getSQDIDByBarCode(paramMap);
//            if(sqdIDMap == null || sqdIDMap.size() == 0){
//                throw new Exception("检验住院计费进入(非条码打印计费)方法,根据条码号未查询到申请单ID,入参【"+map+"】");
//            }
//            paramMap.put("RequestId" , sqdIDMap.get(0).get("DOCTREQUESTNO"));
//            paramMap.put("REQUESTID", sqdIDMap.get(0).get("DOCTREQUESTNO"));
//            Integer mzZyCode = inspectionRelationMapper.queryMzZyCode(paramMap);
//            if(mzZyCode == null){
//                return ResultMessage.error(500,"住院计费失败,未查询到数据【"+map+"】");
//            }
//            if(mzZyCode.intValue() == 1 || mzZyCode.intValue() == 3){
//                return ResultMessage.success(200,"【labRequestInHospitalPayBill】只用于住院检验计费,不能适用于门诊");
//            }
//            requestList.add(paramMap);
        }
        //计费HIS
        //因为存在一个申请单多个条码号，需要按条码收费; 但是会存在两个申请单一个条码
        List<Map<String, Object>> hashMap = inspectionRelationMapper.queryLabRequestPayBillZYByBarCode(paramMap);
        if(hashMap != null && hashMap.size() > 0){
           for (int i = 0; i < hashMap.size(); i++) {
               Map<String,Object> mappara = hashMap.get(i);
               mappara.put("QRGH", gh);
               mappara.put("SRGH", gh);
//             mappara.put("REQUESTID",request.get("REQUESTID"));
               mappara.put("FYRQ", mappara.get("FYRQ").toString().split("\\.")[0]);
               mappara.put("JFRQ", mappara.get("JFRQ").toString().split("\\.")[0]);
           }
           String json = JSON.toJSONString(hashMap);
           ResponseEntity responseEntity = hisServiceFeign.lisPay(json);
           if(responseEntity.getCode() == 400){
               throw new Exception("调用HIS计费失败,操作回滚");
           }
           if(responseEntity.getCode() == 500){
               throw new Exception("住院计费失败："+responseEntity.getMsg());
           }
        }else{
            throw new Exception("该条码号可能已经计费,无法进行住院计费;请核对下! 参数:【"+paramMap+"】");
        }
        for(Map<String, Object> m : hashMap){
            m.put("RequestId" , m.get("REQUESTID"));
            int updateRows = inspectionRelationMapper.updateApplicationBillingMark(m);
            if(updateRows < 1){
                throw new Exception("更新【l_lis_sqdmx】收费状态、执行判别失败");
            }
        }
        int updateRows = inspectionRelationMapper.updateBarCodeBillingMark(paramMap);
        if(updateRows < 1){
            throw new Exception("更新【l_jytmxx_mx】收费状态失败");
        }

//        if(requestList.size() == 0){
//            throw new Exception("住院计费失败,未找到RequestId等参数");
//        }
//        for (Map<String,Object> request : requestList){ //因为存在一个申请单多个条码号，需要按条码收费
////            List<Map<String, Object>> hashMap = inspectionRelationMapper.queryLabRequestPayBillZY(request);
//            List<Map<String, Object>> hashMap = inspectionRelationMapper.queryLabRequestPayBillZYByBarCode(request);
//            if(hashMap != null && hashMap.size() > 0){
//                for (int i = 0; i < hashMap.size(); i++) {
//                    Map<String,Object> mappara = hashMap.get(i);
//                    mappara.put("QRGH", gh);
//                    mappara.put("SRGH", gh);
////                    mappara.put("REQUESTID",request.get("REQUESTID"));
//                    mappara.put("FYRQ", mappara.get("FYRQ").toString().split("\\.")[0]);
//                    mappara.put("JFRQ", mappara.get("JFRQ").toString().split("\\.")[0]);
//                }
//                String json = JSON.toJSONString(hashMap);
//                ResponseEntity responseEntity = hisServiceFeign.lisPay(json);
//                if(responseEntity.getCode() == 400){
//                    throw new Exception("调用HIS计费失败,操作回滚");
//                }
//            }else{
//                throw new Exception("未查询出检验计费添加的参数,无法进行住院计费");
//            }
//        }


//        for (Map<String,Object> request: requestList) {
//            List<Map> prehyidMap = inspectionRelationMapper.getLjytmxxPrehyid(request);
//            for(Map m : prehyidMap){
//                //添加明细完成后需更新状态
//                request.put("PREHYID", m.get("PREHYID"));
//                int updateRows = inspectionRelationMapper.updateApplicationBillingMark(request);
//                if(updateRows < 1){
//                    throw new Exception("更新【l_lis_sqdmx】收费状态、执行判别失败");
//                }
//            }
//            //添加明细完成后需更新状态
//            int updateRows = inspectionRelationMapper.updateBarCodeBillingMark(request);
//            if(updateRows < 1){
//                throw new Exception("更新【l_jytmxx_mx】收费状态失败");
//            }
//        }
        return ResultMessage.success(200,"检验住院计费成功");
    }

    public String getDoctor(Map map ){  // ExeDoctor  ReceiveOperator  CollectOperator
        Map labBarcode = (Map) map.get("LabBarcode"); //
        String doctorNum = "";
        if(labBarcode.get("ExeDoctor") != null ){
            if(labBarcode.get("ExeDoctor") instanceof Map){
                Map ExeDoctor = (Map) labBarcode.get("ExeDoctor");
                doctorNum =  ExeDoctor.get("#text").toString();
            }else {
                doctorNum = labBarcode.get("ExeDoctor").toString();
            }
        }else if(labBarcode.get("ReceiveOperator") != null){
            if(labBarcode.get("ReceiveOperator") instanceof Map){
                Map ReceiveOperator = (Map) labBarcode.get("ReceiveOperator");
                doctorNum =  ReceiveOperator.get("#text").toString();
            }else{
                doctorNum = labBarcode.get("ReceiveOperator").toString();
            }
        }else if(labBarcode.get("CollectOperator") != null){
            if(labBarcode.get("CollectOperator") instanceof Map){
                Map CollectOperator = (Map) labBarcode.get("CollectOperator");
                doctorNum = CollectOperator.get("#text").toString();
            }else{
                doctorNum = labBarcode.get("CollectOperator").toString();
            }
        }else if(labBarcode.get("SendOperator") != null){
            if(labBarcode.get("SendOperator") instanceof Map){
                Map SendOperator = (Map) labBarcode.get("SendOperator");
                doctorNum = SendOperator.get("#text").toString();
            }else{
                doctorNum = labBarcode.get("SendOperator").toString();
            }
        }
        return doctorNum;
    }

}
