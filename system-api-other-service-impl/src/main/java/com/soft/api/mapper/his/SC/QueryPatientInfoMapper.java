package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface QueryPatientInfoMapper {

    public Map<String,Object> queryPatientInfo(Map<String,Object> map);

}
