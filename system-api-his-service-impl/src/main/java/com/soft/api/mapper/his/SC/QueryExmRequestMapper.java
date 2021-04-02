package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface QueryExmRequestMapper {

    /**
     * 根据SQDH 查询出该检查申请单状态
     * @param map
     * @return
     */
    public Map<String,Object> queryExmRequest(Map map);
}
