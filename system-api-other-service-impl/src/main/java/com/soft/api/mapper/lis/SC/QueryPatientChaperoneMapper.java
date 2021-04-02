package com.soft.api.mapper.lis.SC;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface QueryPatientChaperoneMapper {
    /**
     * 查询住院患者陪护者信息
     * @param patientMap
     * @return
     */
    Map<String, Object> queryPatientChaperone(Map patientMap);

    /**
     * 查询住院患者陪护者核算检测结果
     * @param patientMap
     * @return
     */
    List<Map<String, Object>> queryPatientChaperoneNucleateResult(Map patientMap);
}
