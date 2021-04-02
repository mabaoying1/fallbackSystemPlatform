package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ITransfusionReturnBillMapper {

    /**
     * 查询费用数量
     * @param map
     * @return
     */
    public Map getFYSL(Map map);

    /**
     * 新增住院费用明细
     * @param map
     * @return
     */
    public int insertZYFymx(Map map);
}
