package com.soft.api.mapper.his.SC;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryFeeCatalogInfoMapper {

    public List<Map> getFeeCatalog(Map<String,Object> map);
}
