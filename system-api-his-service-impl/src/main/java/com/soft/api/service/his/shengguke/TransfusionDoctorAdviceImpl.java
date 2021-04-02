package com.soft.api.service.his.shengguke;

import com.alibaba.fastjson.JSONObject;
import com.soft.api.mapper.his.shengguke.ITransfusionDoctorAdviceMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 输血医嘱新增 <br>
 * <![CDATA[ <br>
 * <BSXML>  <br>
 * <hospitalId>1</hospitalId>  <br>
 * <outpatient_id>304</outpatient_id> 根据实际情况来看,住院号码/住院号 <br>
 * <patient_dept>15</patient_dept>  <br>
 * <patient_ward>15</patient_ward>  <br>
 * <order_type>211约定医嘱类别</order_type>  <br>
 * <order_itemcode>0</order_itemcode>  <br>
 * <order_itemtext>拟2018年12月10日输O型RH阳性</order_itemtext>  <br>
 * <order_itemcount>1</order_itemcount>  <br>
 * <order_itemunit></order_itemunit>  <br>
 * <order_operperson>322</order_operperson>  <br>
 * <order_operdate>2018-12-10 18:00:00</order_operdate>  <br>
 * <apply_no>585输血系统的单号</apply_no>  <br>
 * <returncode></returncode>  <br>
 * <returnmsg></returnmsg>  <br>
 * <cur_re_data></cur_re_data>  <br>
 * </BSXML>    ]]>  <br>
 * @ClassName TransfusionDoctorAdviceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/6
 * @Version V1.0
 **/
@Service
public class TransfusionDoctorAdviceImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private ITransfusionDoctorAdviceMapper transfusionDoctorAdviceMapper;
    /**
     * 输血医嘱新增
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity transfusionDoctorAdvice(Map map) throws Exception {
        long yzbxh = queryHISKeyCommon.getMaxANDValide("EMR_YZB",1); //医嘱本
        long yzzh = queryHISKeyCommon.getMaxANDValide("EMR_YZB_YZZH",1); //医嘱本_医嘱组号
        long yzbczrz = queryHISKeyCommon.getMaxANDValide("EMR_YZBCZRZ",1); //医嘱本_操作日志
        map.put("yzbxh", yzbxh);
        map.put("yzzh", yzzh);
        map.put("yzbczrz", yzbczrz);
        Map<String,Object> rspMap = new HashMap<>();
        Map<String, Object> patientInfoMap = transfusionDoctorAdviceMapper.getPatientInfo(map);
        if(patientInfoMap == null || patientInfoMap.size() == 0){
            rspMap.put("DEALTYPE", 1);//1挂号2收费3预交
            rspMap.put("ADVICESEQ", 0);
            rspMap.put("ADVICEGROUP", 0);
            return ResultMessage.error(500,"未查询出病人信息【"+map+"】", rspMap);
        }
        map.put("brch" ,patientInfoMap.get("BRCH")) ;
//        map.put("zyhm" ,map.get("outpatient_id")) ;
        map.put("outpatient_id", patientInfoMap.get("ZYH"));
        map.put("order_operdate" , map.get("order_operdate").toString().replaceAll("T"," "));
        int relInt = transfusionDoctorAdviceMapper.insertEmrYzb(map);
        if(relInt < 1){
            throw new Exception("插入医嘱本记录失败;更新记录【0】;入参【"+map+"】");
        }
        relInt = transfusionDoctorAdviceMapper.insertEmrYzbczrz(map);
        if(relInt < 1){
            throw new Exception("插入医嘱本操作日志记录失败;更新记录【0】;入参【"+map+"】");
        }
//        rspMap.put("DEALTYPE", 1);//1挂号2收费3预交
        rspMap.put("ADVICESEQ", yzbxh);
        rspMap.put("ADVICEGROUP", yzzh);
        return ResultMessage.success(200, "输血医嘱新增成功", rspMap);
    }
}
