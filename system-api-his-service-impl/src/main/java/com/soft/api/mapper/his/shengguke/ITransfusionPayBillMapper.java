package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ITransfusionPayBillMapper {

    /**
     * 查询患者信息
     * @param map
     * @return
     */
    public Map getPatientInfo(Map<String,Object> map);

    /**
     * 查询医疗目录
     * @param map
     * @return
     */
    public Map getYLML(Map<String,Object> map);

    /**
     * 查询最大的记录序号
     * @param map
     * @return
     */
    public Map getMaxJlxh(Map<String,Object> map);

    /**
     * 新增住院费用明细
     * @param list
     * @return
     */
    public int insertZYFYMX(List<Map<String, Object>> list);
}
