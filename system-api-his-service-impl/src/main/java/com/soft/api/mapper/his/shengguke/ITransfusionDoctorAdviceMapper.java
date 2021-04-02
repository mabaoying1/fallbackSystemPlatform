package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ITransfusionDoctorAdviceMapper {

    /**
     * 查询患者信息
     * @param map
     * @return
     */
    public Map<String,Object> getPatientInfo(Map<String,Object> map);

    /**
     * 新增医嘱本
     * @param map
     * @return
     */
    public int insertEmrYzb(Map<String,Object> map);

    /**
     * 新增医嘱本操作日志
     * @param map
     * @return
     */
    public int insertEmrYzbczrz(Map<String,Object> map);
}
