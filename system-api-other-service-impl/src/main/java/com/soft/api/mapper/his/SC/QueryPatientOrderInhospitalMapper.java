package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryPatientOrderInhospitalMapper {


    /**
     * 查询预约住院病人信息
     * @param patientMap
     * @return
     */
    public List<Map<String, Object>> queryPatientOrderInhospital(Map patientMap);
}
