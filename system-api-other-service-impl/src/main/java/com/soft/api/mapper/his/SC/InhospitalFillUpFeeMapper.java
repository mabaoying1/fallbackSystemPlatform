package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface InhospitalFillUpFeeMapper {

    /**
     * 获取数据库费用单价
     * @param map
     * @return
     */
    public Map getFydj(Map<String,Object> map);

    /**
     * 获取HIS库中住院号
     * @param map
     * @return
     */
    public Map getZYH(Map<String,Object> map);

    /**
     * 插入住院费用明细
     * @param map
     * @return
     */
    public int insertZyFymx(Map<String,Object> map);

}
