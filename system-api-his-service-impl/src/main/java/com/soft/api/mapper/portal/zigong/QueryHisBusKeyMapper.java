package com.soft.api.mapper.portal.zigong;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @ClassName QueryHisBusKey
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/11
 * @Version V1.0
 **/
@Repository
public interface QueryHisBusKeyMapper {

    public Long getCommonSequence(Map<String, Object> map);

    public int updateCommonSequence(Map<String, Object> map);

}
