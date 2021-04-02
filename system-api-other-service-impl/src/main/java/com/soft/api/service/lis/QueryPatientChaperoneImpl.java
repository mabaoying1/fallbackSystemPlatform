package com.soft.api.service.lis;

import com.soft.api.mapper.lis.SC.QueryPatientChaperoneMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class QueryPatientChaperoneImpl {
    /**
     * 查询住院患者陪护者信息
     */
    @Autowired
    private QueryPatientChaperoneMapper queryPatientChaperoneMapper;
    public ResponseEntity queryPatientChaperone(Map<String, Object> map) {
        try {
            Map patientMap = (Map)map.get("Patient"); // sfzh sampleno doctadviseno  patientid
            boolean bool = false;
            if(patientMap.get("sfzh") != null && !"".equals(patientMap.get("sfzh").toString())){
                bool = true;
            }
            if(patientMap.get("patientid") != null && !"".equals(patientMap.get("patientid").toString())){
                bool = true;
            }

            if(!bool){
                return ResultMessage.error(400,"参数【patientid、sfzh】不能全部为空");
            }
            Map<String, Object> resultMap = queryPatientChaperoneMapper.queryPatientChaperone(patientMap);
            return ResultMessage.success(200,"查询住院患者陪护者信息成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询住院患者陪护者信息失败");
        }
    }

    /**
     * 查询住院患者陪护者核算检测结果
     * @param map
     * @return
     */
    public ResponseEntity queryPatientChaperoneNucleateResult(Map<String, Object> map) {
        try {
            Map patientMap = (Map)map.get("Patient"); // sfzh sampleno doctadviseno  patientid
            boolean bool = false;
            if(patientMap.get("sfzh") != null && !"".equals(patientMap.get("sfzh").toString())){
                bool = true;
            }
            if(patientMap.get("patientid") != null && !"".equals(patientMap.get("patientid").toString())){
                bool = true;
            }

            if(!bool){
                return ResultMessage.error(400,"参数【patientid、sfzh】不能全部为空");
            }
            List<Map<String, Object>> resultMap = queryPatientChaperoneMapper.queryPatientChaperoneNucleateResult(patientMap);
            return ResultMessage.success(200,"查询住院患者陪护者核算检测结果成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询住院患者陪护者核算检测结果失败");
        }
    }
}
