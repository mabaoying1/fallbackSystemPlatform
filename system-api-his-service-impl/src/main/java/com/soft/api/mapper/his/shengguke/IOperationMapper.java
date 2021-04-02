package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IOperationMapper {

    /**
     * 删除手术记录
     * @param map
     * @return
     */
    public int deleteSSJL(Map<String,Object> map);

    /**
     * 新增手术记录
     * @param map
     * @return
     */
    public int insertSSJLReCord(Map<String,Object> map);

    /**
     * 更新手术安排完成标志状态
     * @param map
     * @return
     */
    public int updateSSAPStateWcbz(Map<String,Object> map);

    /**
     * 查询手术记录
     * @param map
     * @return
     */
    public Map<String,Object> queryOpertionWCjlByRequestId(Map<String,Object> map);

    /**
     * 查询手术安排记录
     * @param map
     * @return
     */
    public Map<String,Object> queryOpertionRecordByRequestId(Map<String,Object> map);

    /**
     * 更新手术安排状态
     * @param map
     * @return
     */
    public int updateSSAPState(Map<String,Object> map);

    /**
     * 更新手术申请单状态
     * @param map
     * @return
     */
    public int updateSSSQState(Map<String,Object> map);

    /**
     * 新增手术安排记录
     * @param map
     * @return
     */
    public int addSSAPArrange(Map<String,Object> map);

    /**
     * 查询住院号等相关信息
     * @param map
     * @return
     */
    public Map<String,Object> queryZYH(Map<String,Object> map);

    /**
     * 查询手术安排状态
     * @param map
     * @return
     */
    public Map<String,Object> querySSSQStatus(Map<String,Object> map);

    /**
     * 查询手术安排记录
     * @param map
     * @return
     */
    public Map<String,Object> querySSAPArrange(Map<String,Object> map);
}
