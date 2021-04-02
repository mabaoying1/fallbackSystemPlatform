package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryInhospitalInfoMapper {

    public List<Map<String,Object>> queryInhospitalInfo(Map<String,Object> map);

}
