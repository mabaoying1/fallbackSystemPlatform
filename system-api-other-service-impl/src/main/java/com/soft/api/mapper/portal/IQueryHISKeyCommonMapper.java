package com.soft.api.mapper.portal;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IQueryHISKeyCommonMapper {

    /**
     * 获取DQZ
     * @param map
     * @return
     */
    public String getXH(Map<String,Object> map);

    /**
     * 更新DQZ
     * @param map
     * @return
     */
    public int updateXH(Map<String,Object> map);
}
