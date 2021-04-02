package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ITransfusionDoctorAdviceCancelMapper {

    /**
     * 获取医嘱本
     * @param map
     * @return
     */
    public Map getEmrYzb(Map<String,Object> map);

    /**
     * 获取住院病区医嘱记录条数
     * @param map
     * @return
     */
    public int getBQYZCount(Map<String,Object> map);

    /**
     * 作废住院病区医嘱
     * @param map
     * @return
     */
    public int updateBQYZ(Map<String,Object> map);

    /**
     * 更新医嘱本
     * @param map
     * @return
     */
    public int updateYZB(Map<String,Object> map);

    /**
     * 新增医嘱本操作日志
     * @param map
     * @return
     */
    public int insertYzbczrz(Map<String,Object> map);

}
