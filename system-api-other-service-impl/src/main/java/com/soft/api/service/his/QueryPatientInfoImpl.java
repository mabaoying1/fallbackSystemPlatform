package com.soft.api.service.his;

import com.soft.api.mapper.his.SC.QueryPatientInfoMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 查询业务系统患者信息
 * @ClassName QueryPatientInfoImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/2/24
 * @Version V1.0
 **/
@Service
public class QueryPatientInfoImpl {

    @Autowired
    private QueryPatientInfoMapper queryPatientInfoMapper;

    public ResponseEntity queryPatientInfo(Map<String,Object> map) {
        try {
            Map patientMap = (Map)map.get("Patient"); // sfzh jzkh zyhm  brid
            boolean bool = false;
            if(patientMap.get("sfzh") != null && !"".equals(patientMap.get("sfzh").toString())){
                bool = true;
            }
            if(patientMap.get("zyhm") != null && !"".equals(patientMap.get("zyhm").toString())){
                bool = true;
            }
            if(patientMap.get("brid") != null && !"".equals(patientMap.get("brid").toString())){
                bool = true;
            }
            if(!bool){
                return ResultMessage.error(400,"参数【sfzh、zyhm、brid】不能全部为空");
            }
            Map<String, Object> resultMap = queryPatientInfoMapper.queryPatientInfo(patientMap);
            return ResultMessage.success(200,"查询业务系统患者信息成功",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询业务系统患者信息失败");
        }
    }
}
