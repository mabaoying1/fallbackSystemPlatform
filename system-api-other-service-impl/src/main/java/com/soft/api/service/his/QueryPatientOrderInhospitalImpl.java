package com.soft.api.service.his;

import com.soft.api.mapper.his.SC.QueryPatientInfoMapper;
import com.soft.api.mapper.his.SC.QueryPatientOrderInhospitalMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * 查询预约住院病人信息
 * @ClassName QueryPatientOrderInhospitalImpl
 * @Description: TODO
 * @Author ljx
 * @Date 2021/3/03
 * @Version V1.0
 **/
@Service
public class QueryPatientOrderInhospitalImpl {
    @Autowired
    private QueryPatientOrderInhospitalMapper queryPatientOrderInhospitalMapper;

    /**
     * 查询预约住院病人信息
     * @param map
     * @return
     */
    public ResponseEntity QueryPatientOrderInhospital(Map<String, Object> map) {
        try {
            Map patientMap = (Map)map.get("Patient"); // sfzh brid
            boolean bool = false;
            if(patientMap.get("sfzh") != null && !"".equals(patientMap.get("sfzh").toString())){
                bool = true;
            }
            if(patientMap.get("brid") != null && !"".equals(patientMap.get("brid").toString())){
                bool = true;
            }

            if(!bool){
                return ResultMessage.error(400,"参数【brid、sfzh】不能全部为空");
            }

            List<Map<String, Object>> resultMap = queryPatientOrderInhospitalMapper.queryPatientOrderInhospital(patientMap);
            return ResultMessage.success(200,"查询预约住院病人信息成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询预约住院病人信息失败");
        }
    }
}
