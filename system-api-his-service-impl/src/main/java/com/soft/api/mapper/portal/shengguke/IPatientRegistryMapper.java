package com.soft.api.mapper.portal.shengguke;


import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IPatientRegistryMapper {

    /**
     * 新增公用病人档案
     * @param map
     * @return
     */
    public int addGyBrdaPortal(Map<String,Object> map);

}
