package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IPatientUpdateImplMapper {


    /**
     * 查询默认的病人性质
     * @param map
     * @return
     */
    public Map getDefaultBRXZ(Map<String,Object> map);

    /**
     * 查询是否已经建档
     * @param map
     * @return
     */
    public int getExistEmployeeByIDCard(Map<String,Object> map);

    /**
     * 更新MS_BRDA
     * @param map
     * @return
     */
    public int updateBrda(Map<String,Object> map);

    /**
     * 更新KH_BRDA
     * @param map
     * @return
     */
    public int updateKHBrda(Map<String,Object> map);
}
