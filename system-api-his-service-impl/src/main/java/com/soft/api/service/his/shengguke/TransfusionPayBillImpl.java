package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.ITransfusionPayBillMapper;
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
 * 输血费用计费 <br>
 * <![CDATA[ <br>
 * <BSXML> <br>
 * <hospitalId>1</hospitalId> <br>
 * <rowdata> <br>
 *  <outpatient_id>病人ID</outpatient_id> <br>
 * 	<patient_dept>医嘱生成时科室</patient_dept> <br>
 * 	<patient_ward>医嘱生成时病人</patient_ward> <br>
 * 	<patient_exec>执行科室</patient_exec> <br>
 * 	<exec_source>数据来源</exec_source> <br>
 * 	<item_date>费用日期</item_date> <br>
 * 	<item_code>费用项目内码ID</item_code> <br>
 * 	<order_itemcount>费用数量</order_itemcount> <br>
 * 	<order_itemprice>单价</order_itemprice> <br>
 * 	<order_operperson>医嘱生成时医生</order_operperson> <br>
 * 	<order_inputperson>执行人</order_inputperson> <br>
 * 	<apply_no>申请单号</apply_no> <br>
 * </rowdata> <br>
 * <rowdata> <br>
 * </rowdata> <br>
 * </BSXML>   <br>
 * @ClassName TransfusionPayBillImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/7
 * @Version V1.0
 **/

@Service
public class TransfusionPayBillImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private ITransfusionPayBillMapper transfusionPayBillMapper;

    /**
     * 输血费用计费
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity transfusionPayBill(Map map) throws Exception {
        List<Map<String,Object>> listMap = null;
        List<Map<String, Object>> responseResult =new ArrayList<Map<String, Object>>();
        if (map.get("rowdata") instanceof List) {
            listMap = (ArrayList<Map<String,Object>>) map.get("rowdata");
        } else {
            listMap = new ArrayList<>();
            Map rowdataMap = (Map) map.get("rowdata");
            listMap.add(rowdataMap);
        }
        for(Map<String,Object> m : listMap){
            Map patientInfoMap = transfusionPayBillMapper.getPatientInfo(m);
            if(patientInfoMap == null || patientInfoMap.size() == 0){
                return ResultMessage.error(500,"未查询到患者信息;入参【"+map+"】");
            }
            if(Integer.parseInt(patientInfoMap.get("CYPB").toString()) >= 8){
                return ResultMessage.error(500,"该患者处于结算或作废状态,不能计费;入参【"+map+"】");
            }
            m.put("outpatient_id", patientInfoMap.get("ZYH"));
            Map ylmlMap = transfusionPayBillMapper.getYLML(m);
            if(ylmlMap == null || ylmlMap.size() == 0){
                return ResultMessage.error(500,"未查询到项目代码;入参【"+map+"】");
            }
            Map jlxhMap = transfusionPayBillMapper.getMaxJlxh(m);
            if(jlxhMap == null || jlxhMap.size() == 0){
                return ResultMessage.error(500,"医嘱【未复核或者已作废】,未查询到医嘱表记录序号;入参【"+map+"】");
            }
            m.put("fygb" , ylmlMap.get("FYGB"));
            m.put("fymc" , ylmlMap.get("FYMC"));
            m.put("yzxh" , jlxhMap.get("JLXH"));
            m.put("brbq" , patientInfoMap.get("BRBQ"));
            long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX" ,1);
            m.put("jlxh" , jlxh);
            Map<String, Object> response = new HashMap<>();
            response.put("OUTPATIENTID", m.get("outpatient_id"));
            response.put("ITEMCODE", m.get("item_code"));
            response.put("CHARGESEQ", String.valueOf(jlxh));
            responseResult.add(response);
        }
        int rel = transfusionPayBillMapper.insertZYFYMX(listMap);
        if(rel < 1){
            return ResultMessage.error(500,"输血计费失败,新增记录【0】;入参【"+map+"】");
        }
        return ResultMessage.success(200,"输血计费成功",responseResult);
    }

}
