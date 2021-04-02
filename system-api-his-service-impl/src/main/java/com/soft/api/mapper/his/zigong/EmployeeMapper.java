package com.soft.api.mapper.his.zigong;

import org.springframework.stereotype.Repository;
import java.util.Map;
@Repository
public interface EmployeeMapper {

    public int insertEmployee(Map<String,Object> paramMap);

    public int updateEmployee(Map<String,Object> paramMap);
}
