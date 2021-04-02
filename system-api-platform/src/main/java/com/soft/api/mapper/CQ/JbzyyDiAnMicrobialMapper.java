package com.soft.api.mapper.CQ;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 〈功能概述〉<br>
 *
 * @className: JbzyyDiAnMicrobialMapper
 * @package: com.soft.api.mapper.CQ
 * @author: Ljx
 * @date: 2021/03/22 11:11
 */
@Repository
@Mapper
@Component
public interface JbzyyDiAnMicrobialMapper {
    public Map<String,Object> getSampleType(Map<String,Object> map);

    public Map<String,Object> getTestIDByDiAn(Map<String,Object> map);

    public int insertAntiResult(Map<String,Object> map);

    public int updateAntiResult(Map<String,Object> map);

    public int getAntiResultCount(Map<String,Object> map);

    public Map<String,Object> getAntiDic(Map<String,Object> map);

    public int insertBioResult(Map<String,Object> map);

    public int updateBioResult(Map<String,Object> map);

    public int getBioResultCount(Map<String,Object> map);

    public int insertPlantResult(Map<String,Object> map);

    public Map<String,Object> getPatientInfo(Map<String,Object> map);

    public int updatePlantResult(Map<String,Object> map);

    public int getPlantByNoCounts(Map<String,Object> map);

    public int getPatientInfoByNoCounts(Map<String,Object> map);

    public Map<String,Object> getBarcodeByDiAnClinicid(Map<String,Object> map);
}
