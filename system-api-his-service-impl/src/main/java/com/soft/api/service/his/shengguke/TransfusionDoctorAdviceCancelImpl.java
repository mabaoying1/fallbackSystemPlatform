package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.ITransfusionDoctorAdviceCancelMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 输血医嘱撤回(作废)  <br>
 * <![CDATA[  <br>
 * <BSXML>  <br>
 * <hospitalId>1</hospitalId>  <br>
 * <outpatient_id>304</outpatient_id>  <br>
 * <orderno>8406657</orderno>  <br>
 * <operperson>322</operperson>  <br>
 * <operreason>患者不需要</operreason>  <br>
 * <operdate>2018-12-11 11:19:00</operdate>  <br>
 * <returncode></returncode>  <br>
 * <returnmsg></returnmsg>  <br>
 * <cur_re_data></cur_re_data>  <br>
 * </BSXML>    ]]>  <br>
 * @ClassName TransfusionDoctorAdviceCancelImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/6
 * @Version V1.0
 **/
@Service
public class TransfusionDoctorAdviceCancelImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private ITransfusionDoctorAdviceCancelMapper transfusionDoctorAdviceCancelMapper;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity transfusionDoctorAdviceCancel(Map map) throws Exception {
        Map yzbMap = transfusionDoctorAdviceCancelMapper.getEmrYzb(map);
        if(yzbMap == null || yzbMap.size() == 0){
            return ResultMessage.error(500,"未查询到医嘱本信息;【"+map+"】");
        }
        if("3".equals(yzbMap.get("YZZT").toString())){
            return ResultMessage.error(500,"医嘱号已作废;【"+map+"】");
        }
        if(!yzbMap.get("KZKS").toString().equals(map.get("patient_dept").toString())){
            return ResultMessage.error(500,"作废科室与开嘱科室不一致;【"+map+"】");
        }
        //如果复核了也要ZY_BQYZ也作废
        int bqyzCount = transfusionDoctorAdviceCancelMapper.getBQYZCount(map);
        if(bqyzCount > 0){
            int relInt = transfusionDoctorAdviceCancelMapper.updateBQYZ(map);
            if(relInt < 1){
                return ResultMessage.error(500,"更新住院病区医嘱失败,更新记录数【0】;【"+map+"】");
            }
        }
        long yzbczrz = queryHISKeyCommon.getMaxANDValide("EMR_YZBCZRZ" , 1);
        map.put("yzbczrz" , yzbczrz);
        int relInt = transfusionDoctorAdviceCancelMapper.updateYZB(map);
        if(relInt < 1){
            throw new Exception("更新医嘱本记录失败,更新记录数【0】;入参【"+map+"】");
        }
        relInt = transfusionDoctorAdviceCancelMapper.insertYzbczrz(map);
        if(relInt < 1){
            throw new Exception("新增医嘱本操作日志失败,新增记录数【0】;入参【"+map+"】");
        }
        return ResultMessage.success(200, "医嘱作废成功");
    }

}
