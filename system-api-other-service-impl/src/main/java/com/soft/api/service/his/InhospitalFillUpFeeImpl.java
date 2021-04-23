package com.soft.api.service.his;

import com.soft.api.mapper.his.SC.InhospitalFillUpFeeMapper;
import com.soft.api.util.SystemDateSQLUtil;
import ctd.net.rpc.util.FieldVerifyEmptyUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName InhospitalFillUpFeeImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Service
public class InhospitalFillUpFeeImpl {

    @Autowired
    private InhospitalFillUpFeeMapper inhospitalFillUpFee;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private SystemDateSQLUtil systemDateSQLUtil;

    @Transactional
    public ResponseEntity inhospitalFillUpFee(Map<String,Object> map) {
        try {
            Map feeMap = (Map)map.get("Fee");
            String relStr = FieldVerifyEmptyUtil.verifyEmpty(feeMap,null);
            if(StringUtils.isNotBlank(relStr)){
                return ResultMessage.error(500,relStr);
            }
            long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX", 1);
            feeMap.put("jlxh" , jlxh);
            feeMap.put("fyrq" , systemDateSQLUtil.getSystemDateTime());
            Map zyhMap = inhospitalFillUpFee.getZYH(feeMap);
            if(zyhMap == null || zyhMap.size() == 0){
                return ResultMessage.error(500,"住院号码有误,不能找到对应的住院号");
            }
            feeMap.put("zyh" , zyhMap.get("ZYH"));
            Map fydjMap = inhospitalFillUpFee.getFydj(feeMap);
            if(fydjMap == null || fydjMap.size() == 0){
                return ResultMessage.error(500,"费用序号有误,不能找到对应的费用");
            }
            //从数据库获取费用单价
            BigDecimal fydj = new BigDecimal(fydjMap.get("FYDJ").toString());
            BigDecimal zjje = new BigDecimal(feeMap.get("zjje").toString());
            BigDecimal fysl = new BigDecimal(feeMap.get("fysl").toString());
            if(fysl.compareTo(BigDecimal.ZERO) == 0 || fysl.compareTo(BigDecimal.ZERO) == -1){
                return ResultMessage.error(500,"计费数量不能小于等于0");
            }
            BigDecimal tempZjje = fydj.multiply(fysl);
            if(tempZjje.compareTo(zjje) != 0){
                return ResultMessage.error(500,"计费参数有误,传入金额不等于计算金额;请核对项目单价是否已更改");
            }
            feeMap.put("fymc" ,fydjMap.get("FYMC"));
            int rel = inhospitalFillUpFee.insertZyFymx(feeMap);
            if(rel > 0){
                Map relMap = new HashMap();
                relMap.put("jlxh" , jlxh);
                relMap.put("zjje" , tempZjje);
                return ResultMessage.success(200,"住院补计费记录成功",relMap);
            }
            return ResultMessage.error(500,"住院补计费记录失败,新增数据【0】");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"住院补计费记录失败");
        }
    }
}
