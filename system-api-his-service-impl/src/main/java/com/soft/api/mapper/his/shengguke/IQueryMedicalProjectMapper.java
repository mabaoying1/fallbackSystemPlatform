package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IQueryMedicalProjectMapper {

    /**
     * 获取医疗目录
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMedicalProject(Map<String,Object> map);
}
