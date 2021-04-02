package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IOperationMapper;
import ctd.net.rpc.util.MapUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * 手术相关操作
 * @ClassName OperationImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/30
 * @Version V1.0
 **/
@Service("operationSGK")
public class OperationImpl {

    @Autowired
    private IOperationMapper operationMapper;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    /**
     * 取消手术完成通知
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity optCompleteCancel(Map<String,Object> paramMap){
        Map request = (Map)paramMap.get("OptRequest");
        request.put("wcbz", 0);
        request.put("mzbz", 1);
        operationMapper.updateSSAPStateWcbz(request);
        Map ssapMap = operationMapper.queryOpertionRecordByRequestId(request);
        if (ssapMap != null && ssapMap.size() > 0) {
            request.put("SSBH", ssapMap.get("ssbh").toString());
            operationMapper.deleteSSJL(request);
        }
        return ResultMessage.success(200,"取消手术完成通知成功");
    }


    /**
     * 手术完成通知
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity optComplete(Map<String,Object> paramMap){
        Map request = (Map) paramMap.get("OptRequest");
        String  requestId=(String)request.get("RequestId");
        if (requestId==null || requestId.equals("") ) {
            return ResultMessage.error(500,"请填写手术申请单号,【"+paramMap+"】");
        }
        Map ssapMap = operationMapper.queryOpertionRecordByRequestId(request);
        if (ssapMap == null || ssapMap.isEmpty()) {
            return ResultMessage.error(500,"无手术安排记录,无法结束手术,【"+paramMap+"】");
        }
        Map ssjlMap = operationMapper.queryOpertionWCjlByRequestId(request);
        if(ssjlMap != null && ssjlMap.size() > 0){
            return ResultMessage.error(500,"该患者手术记录已处于完成状态,无需重复操作,【"+paramMap+"】");
        }
        request.put("OptPatientType", "1");
        request.put("KSSJ", request.get("OperationStartDateTime").toString().replaceAll("T"," "));
        request.put("JSSJ", request.get("OperationEndDateTime").toString().replaceAll("T"," "));
//        request.put("SSFJ", request.get("ASALevel").toString());
        request.put("ZYH", ssapMap.get("zyh").toString());
        request.put("SSBH", ssapMap.get("ssbh").toString());
        request.put("wcbz", 1);
        request.put("mzbz", 1);
        request.put("Operator",MapUtil.getDicAttr("Operator", "text", request));
        operationMapper.updateSSAPStateWcbz(request);
        operationMapper.insertSSJLReCord(request);
        return ResultMessage.success(200,"手术完成通知成功");
    }

    /**
     * 取消手术安排通知
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity  optArrangeCancel(Map<String,Object> paramMap){
        Map request = (Map) paramMap.get("OptRequest");
        request.put("apbz", 0);
        request.put("zfbz", 1);
        operationMapper.updateSSAPState(request);
        operationMapper.updateSSSQState(request);
        return ResultMessage.success(200,"取消手术安排成功");
    }

    /**
     * 手术安排通知
     * @param paramMap
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity optArrange(Map<String,Object> paramMap) throws Exception {
        Map request = (Map) paramMap.get("OptRequest");
        Map mapStatus = operationMapper.querySSSQStatus(request);
        if(mapStatus == null || mapStatus.isEmpty()){
            return ResultMessage.error(500,"根据SQDH未查询到手术安排,【"+paramMap+"】");
        }
        if(mapStatus.get("ZFBZ") == null || "".equals(mapStatus.get("ZFBZ"))
                || "1".equals(mapStatus.get("ZFBZ").toString()) ){
            return ResultMessage.error(500,"该手术申请已作废,【"+paramMap+"】");
        }
        Map arrmap = operationMapper.querySSAPArrange(request);
        if (arrmap == null || arrmap.isEmpty()) {
           long DQZ = queryHISKeyCommon.getMaxANDValide("SM_SSAP" , 1);
           request.put("DQZ", DQZ);
           request.put("czgh", ""); //OperationDateTime -> 20130116T112855
           request.put("OperationDateTime",  request.get("OperationDateTime").toString().replaceAll("T"," "));
           Map zyhMap = operationMapper.queryZYH(request);
           request.put("ZYH", zyhMap.get("zyh"));
           request.put("OPERATION", zyhMap.get("operationCode"));
           request.put("apbz", 1);
           request.put("JZBZ", 0);
           request.put("WCBZ", 0);
           request.put("MZWCBZ", 1);
           request.put("AnesthesiaWayCode", zyhMap.get("anesthesiaWayCode"));
           request.put("Operator",
                    MapUtil.getDicAttr("Operator", "text", request));
           request.put("AnesthesiaDoctor",
                    MapUtil.getDicAttr("AnesthesiaDoctor", "text", request));
           request.put("OperationDept",
                    MapUtil.getDicAttr("OperationDept", "text", request));

           if(request.get("Assistant1") instanceof Map){
               Map assistant1Map = (Map) request.get("Assistant1");
               request.put("Assistant1" , assistant1Map.get("#text").toString());
           }
           if(request.get("Assistant2") instanceof Map){
               Map assistant2Map = (Map) request.get("Assistant2");
               request.put("Assistant2" , assistant2Map.get("#text").toString());
           }
           if(request.get("Assistant3") instanceof Map){
               Map assistant3Map = (Map) request.get("Assistant3");
               request.put("Assistant3" , assistant3Map.get("#text").toString());
           }

           operationMapper.addSSAPArrange(request);
           operationMapper.updateSSSQState(request);
           return ResultMessage.success(200,"手术安排成功");
        } else{
           return ResultMessage.error(500,"该患者已完成手术安排,不能重复安排");
        }
    }
}
