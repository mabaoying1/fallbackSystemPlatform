package com.soft.api.mapper.portal.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IPatientUpdateImplMapper {

    /**
     * 更新KH_BRDA
     * @param map
     * @return
     */
    public int updateGYBrda(Map<String,Object> map);
}
