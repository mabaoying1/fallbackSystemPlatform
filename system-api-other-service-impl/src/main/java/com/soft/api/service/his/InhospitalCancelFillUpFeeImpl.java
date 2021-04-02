package com.soft.api.service.his;

import com.soft.api.mapper.his.SC.InhospitalCancelFillUpFeeMapper;
import ctd.net.rpc.util.FieldVerifyEmptyUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import ctd.net.rpc.util.SystemDateSQLUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 住院作废补计费信息
 * @ClassName InhospitalCancelFillUpFeeImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/3/24
 * @Version V1.0
 **/
@Service
public class InhospitalCancelFillUpFeeImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private SystemDateSQLUtil systemDateSQLUtil;

    @Autowired
    private InhospitalCancelFillUpFeeMapper inhospitalCancelFillUpFeeMapper;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity inhospitalCancelFillUpFee(Map<String,Object> map) {
        try {
            Map feeMap = (Map)map.get("Fee");
            long oldJlxh = Long.parseLong(feeMap.get("jlxh").toString());
            long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX", 1);
            String relStr = FieldVerifyEmptyUtil.verifyEmpty(feeMap,null);
            if(StringUtils.isNotBlank(relStr)){
                return ResultMessage.error(500,relStr);
            }
            Map zyhMap = inhospitalCancelFillUpFeeMapper.getZYH(feeMap);
            if(zyhMap == null || zyhMap.size() == 0){
                return ResultMessage.error(500,"住院号码有误,不能找到对应的住院号");
            }
            zyhMap.put("jlxh" , oldJlxh);
            Map oldFeeMap = inhospitalCancelFillUpFeeMapper.queryZyFymx(zyhMap);
            //从数据库获取费用单价
//            BigDecimal oldZjje = new BigDecimal(oldFeeMap.get("zjje").toString());
            BigDecimal zjje = new BigDecimal(feeMap.get("zjje").toString());
//            if(oldZjje.compareTo(zjje) != 0){
//                return ResultMessage.error(500,"退费参数有误,金额未能与计费金额匹配;请核对");
//            }
            if(oldFeeMap == null || oldFeeMap.size() == 0){
                return ResultMessage.error(500,"入参jlxh有误,未能查询出已计费数据");
            }

            BigDecimal oldfydj = new BigDecimal(oldFeeMap.get("fydj").toString());
            BigDecimal oldFysl = new BigDecimal(oldFeeMap.get("fysl").toString());
            BigDecimal oldTfsl = new BigDecimal(oldFeeMap.get("tfsl").toString());
            BigDecimal tfsl = new BigDecimal(feeMap.get("tfsl").toString());
            if(tfsl.compareTo(BigDecimal.ZERO) == 0 || tfsl.compareTo(BigDecimal.ZERO) == -1){
                return ResultMessage.error(500,"退费数量不能小于等于0");
            }
            if(oldFysl.compareTo((oldTfsl.add(tfsl))) == -1){
                return ResultMessage.error(500,"退费超过计费数量,退费失败");
            }
            if(zjje.compareTo( (oldfydj.multiply(tfsl)) ) != 0 ){
                return ResultMessage.error(500,"退费金额与数据库退款金额不匹配,退费失败");
            }
            BigDecimal oldTfje = new BigDecimal(oldFeeMap.get("tfje").toString());
            feeMap.put("tfsl" , oldTfsl.add(tfsl));
            feeMap.put("tfje" , oldTfje.add(oldfydj.multiply(tfsl)));
            oldFeeMap.put("zyh" , zyhMap.get("ZYH"));
            oldFeeMap.put("fysl" , "-"+tfsl);
            oldFeeMap.put("zjje" , "-"+oldfydj.multiply(tfsl));
            oldFeeMap.put("fyrq" , systemDateSQLUtil.getSystemDateTime());
            oldFeeMap.put("jlxh" , jlxh);
            oldFeeMap.put("ysgh" , feeMap.get("ysgh"));
            oldFeeMap.put("srgh" , feeMap.get("srgh"));
            oldFeeMap.put("qrgh" , feeMap.get("qrgh"));
            int rel = inhospitalCancelFillUpFeeMapper.updateZyFymx(feeMap);
            if(rel == 0){
                throw new Exception("住院作废补计费记录失败,更新数据【0】");
            }
            rel = inhospitalCancelFillUpFeeMapper.insertZyFymx(oldFeeMap);
            if(rel == 0){
                throw new Exception("住院作废补计费记录失败,新增数据【0】");
            }
            Map relMap = new HashMap();
            relMap.put("jlxh" , jlxh);
            relMap.put("zjje" , zjje);
            return ResultMessage.success(200,"住院作废补计费记录成功",relMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"住院作废补计费记录失败");
        }
    }

}
