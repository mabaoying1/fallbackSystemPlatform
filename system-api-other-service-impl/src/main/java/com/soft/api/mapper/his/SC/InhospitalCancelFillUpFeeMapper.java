package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InhospitalCancelFillUpFeeMapper {

    /**
     * 更新住院费用明细的退费数量、退费金额
     * @param map
     * @return
     */
    public int updateZyFymx(Map<String,Object> map);

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
     * 查询住院费用明细
     * @return
     */
    public Map<String, Object> queryZyFymx(Map<String,Object> map);

    /**
     * 插入住院费用明细
     * @param map
     * @return
     */
    public int insertZyFymx(Map<String,Object> map);

}
