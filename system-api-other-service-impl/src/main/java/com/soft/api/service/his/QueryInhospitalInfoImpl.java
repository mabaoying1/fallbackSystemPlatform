package com.soft.api.service.his;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soft.api.mapper.his.SC.QueryInhospitalInfoMapper;

import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;

/**
 * 查询业务系统患者住院记录
 * @author xw
 * 2021年2月25日10:47:15
 *
 */
@Service
public class QueryInhospitalInfoImpl { 

    @Autowired
    private QueryInhospitalInfoMapper queryInoInfoMapper;

    public ResponseEntity queryInhospitalInfo(Map<String,Object> map) {
        try {
            Map patientMap = (Map)map.get("Patient"); // sfzh zyhm brid
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
            List<Map<String, Object>> resultList = queryInoInfoMapper.queryInhospitalInfo(patientMap);
            return ResultMessage.success(200,"查询病人信息成功",resultList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询病人信息失败");
        }
    }
}
