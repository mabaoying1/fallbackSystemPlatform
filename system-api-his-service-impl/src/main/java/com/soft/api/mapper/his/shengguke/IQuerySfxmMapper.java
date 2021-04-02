package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IQuerySfxmMapper {

    public List<Map> getSFXM(Map<String,Object> map);
}
