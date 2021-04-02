package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.ITransfusionReturnBillMapper;
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
 * 输血取消计费 <br>
 * <![CDATA[ <br>
 *  <BSXML> <br>
 * 		<hospitalId>1</hospitalId> <br>
 * 			<rowdata> <br>
 * 			     <outpatient_id>66341</outpatient_id> <br>
 * 				<chargeseq>28285526</chargeseq> <br>
 * 				<order_rtnqty>-1</order_rtnqty> <br>
 * 				<order_operPerson>202</order_operPerson> <br>
 * 		    </rowdata> <br>
 * <rowdata> <br>
 * </rowdata> <br>
 *</BSXML> ]]> <br>
 * @ClassName TransfusionReturnBillImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/7
 * @Version V1.0
 **/
@Service
public class TransfusionReturnBillImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private ITransfusionReturnBillMapper transfusionReturnBillMapper;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity transfusionReturnBill(Map map) throws Exception {
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
            if(Double.valueOf( m.get("order_rtnqty").toString())>0) {
                return ResultMessage.error(500, "正向数据["+m.get("chargeseq").toString()+"]退费操作退费数据不能大于0");
            }
            Map fyslMap = transfusionReturnBillMapper.getFYSL(m);
            if(fyslMap == null || fyslMap.size() == 0){
                return ResultMessage.error(500, "未查询到费用明细中费用数量;入参【"+map+"】");
            }
            Double fysl = Double.valueOf(fyslMap.get("FYSL").toString());
            if(fysl - Math.abs(Double.valueOf( m.get("order_rtnqty").toString())) < 0){
                return ResultMessage.error(500, "传入正向数据ID【"+m.get("chargeseq")+"】,余额为【"+fysl+"】,不足以退【"+Math.abs(Integer.parseInt(m.get("order_rtnqty").toString()))+"】");
            }
            long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX", 1);
            m.put("jlxh" , jlxh);
            int rel = transfusionReturnBillMapper.insertZYFymx(m);
            if(rel < 1){
                return ResultMessage.error(500, "输血退费失败,更新记录数【0】;入参【"+map+"】");
            }
            Map<String, Object> response = new HashMap<>();
            response.put("CHARGESEQ",m.get("chargeseq"));
            response.put("CHARGESEQRTN" , jlxh);
            responseResult.add(response);
        }
        return ResultMessage.success(200,"输血退费成功", responseResult);
    }

}
