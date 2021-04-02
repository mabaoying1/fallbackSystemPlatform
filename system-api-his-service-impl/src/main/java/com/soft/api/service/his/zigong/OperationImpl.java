package com.soft.api.service.his.zigong;

import com.soft.api.mapper.his.zigong.OperationMapper;
import ctd.net.rpc.util.DateUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @ClassName OperationImpl
 * @Description: TODO 手术相关的功能
 * @Author caidao
 * @Date 2020/11/11
 * @Version V1.0
 **/
@Service
public class OperationImpl {

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private QueryHisBusKey queryHisBusKey;


    public ResponseEntity optCompleteCancel(Map<String,Object> paramMap){
        try {
            if (paramMap.get("RequestId") == null || "".equals(paramMap.get("RequestId").toString())) {
                return ResultMessage.error("RequestId节点不能为空");
            }
            operationMapper.updateArrangeWC(paramMap);
            return ResultMessage.success("手术取消完成成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error("取消手术完成通知异常,请联系管理员;"+e.getMessage());
        }
    }


    public ResponseEntity operationCompleted(Map<String,Object> paramMap){
        try{
            if(paramMap.get("RequestId") == null || "".equals(paramMap.get("RequestId").toString())){
                return ResultMessage.error("RequestId节点不能为空");
            }
            Map<String,Object> queryOpertionMap =  operationMapper.queryOpertionRecordByRequestId(paramMap);
            if(queryOpertionMap == null || queryOpertionMap.size() == 0){
                return ResultMessage.error("无手术安排记录,无法结束手术");
            }
            Map<String,Object> queryOpertionWCJLMap =  operationMapper.queryOpertionWCjlByRequestId(paramMap);
            if(queryOpertionWCJLMap == null || queryOpertionWCJLMap.size() == 0){
                return ResultMessage.error("该患者手术记录已处于完成状态,无需重复操作");
            }
            paramMap.put("OptPatientType", "1");
            paramMap.put("KSSJ", DateUtil.stringToDate(paramMap.get(
                    "OperationStartDateTime").toString()));
            paramMap.put("JSSJ", DateUtil.stringToDate(paramMap.get(
                    "OperationEndDateTime").toString()));
            paramMap.put("SSFJ", paramMap.get("ASALevel").toString());
            paramMap.put("ZYH", queryOpertionMap.get("ZYH").toString());
            paramMap.put("SSBH", queryOpertionMap.get("SSBH").toString());
            paramMap.put("wcbz", 1);
            paramMap.put("mzbz", 1);
            operationMapper.updateArrangeSSMZ(paramMap);
            operationMapper.insertOperationReCordOld(paramMap);
            return ResultMessage.success("手术结束安排成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error("手术完成通知异常,请联系管理员;"+e.getMessage());
        }
    }


    @Transactional
    public ResponseEntity optArrangeCancel(Map<String,Object> paramMap){
        try{
            if(paramMap.get("RequestId") == null || "".equals(paramMap.get("RequestId").toString())){
                return ResultMessage.error("RequestId节点不能为空");
            }
            paramMap.put("apbz", 0);
            paramMap.put("zfbz", 1);
            operationMapper.updateArrangeStatus(paramMap);
            operationMapper.updateArrangeBZ(paramMap);
            return ResultMessage.success("取消手术安排通知成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error("取消手术安排通知异常,请联系管理员;"+e.getMessage());
        }
    }


    @Transactional
    public ResponseEntity optArrange(Map<String,Object> paramMap){
        try {
            if(paramMap.get("RequestId") == null || "".equals(paramMap.get("RequestId").toString())){
                return ResultMessage.error("RequestId节点不能为空");
            }
            Map<String,Object> statusMap = operationMapper.queryStatus(paramMap);
            if(statusMap == null || statusMap.isEmpty()){
                return ResultMessage.error("根据SQDH未查询到手术安排");
            }
            if(statusMap.get("ZFBZ") == null || "".equals(statusMap.get("ZFBZ"))
                    || "1".equals(statusMap.get("ZFBZ")) ){
                return ResultMessage.error("该手术申请已作废");
            }
            Map<String,Object> arrMap = operationMapper.querySMSSAPInfo(paramMap);
            if (arrMap == null || arrMap.isEmpty()) {
                ResponseEntity responseEntity = queryHisBusKey.queryHisBusKey("SM_SSAP");
                if(responseEntity.getCode() == 200){
                    paramMap.put("DQZ" , responseEntity.getResult());
                    paramMap.put("czgh", "");
                    paramMap.put("OperationDateTime", DateUtil.stringToDate(paramMap.get(
                            "OperationDateTime").toString()));
                    //获取住院号等信息
                    Map<String, Object> zyhMap = operationMapper.queryZYH(paramMap);
                    paramMap.put("ZYH", zyhMap.get("ZYH"));
                    paramMap.put("OPERATION" , zyhMap.get("operationCode"));
                    paramMap.put("AnesthesiaWayCode", zyhMap.get("anesthesiaWayCode"));
                    paramMap.put("apbz", 1);
                    paramMap.put("JZBZ", 0);
                    paramMap.put("WCBZ", 0);
                    paramMap.put("MZWCBZ", 1);
                    operationMapper.addArrange(paramMap);
                    operationMapper.updateArrange(paramMap);
                    return ResultMessage.success("手术安排成功");
                }else{
                    return responseEntity;
                }
            }else{
                return ResultMessage.error("该患者已完成手术安排,不能重复进行手术安排!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error("手术安排异常,请联系管理员;"+e.getMessage());
        }
    }

}
