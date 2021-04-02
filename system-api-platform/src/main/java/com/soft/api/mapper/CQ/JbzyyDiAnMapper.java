package com.soft.api.mapper.CQ;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 〈迪安一般检验结果描述〉<br>
 *
 * @className: JbzyyDiAnMapper
 * @package: com.soft.api.mapper
 * @author: Ljx
 * @date: 2021/03/19 15:28
 */
@Repository
@Mapper
@Component
public interface JbzyyDiAnMapper {
    public Map<String,Object> getSampleType(Map<String,Object> map);

    public Map<String,Object> getTestIDByDiAnS(Map<String,Object> map);

    public int deleteTestResult(Map<String,Object> map);

    public Map<String,Object> getBarcodeByDiAnClinicid(Map<String,Object> Map);

    public int insertPatientInfo(Map<String,Object> Map);

    public Map<String,Object> getPatientInfoMap(Map<String,Object> Map);

    public void insertL_TestResult(List<Map<String,Object>> listDataMap);

    public int getPatientInfoByNoCounts(Map<String,Object> Map);

    public int deletePatientInfoByNo(Map<String,Object> Map);

    public int updateJytmxx(Map<String, Object> vMap);

    public int updatePatientInfo(Map<String, Object> patientPara);
}
