package com.soft.api.service.his.zigong;

import com.soft.api.mapper.his.zigong.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @ClassName EmployeeImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/6
 * @Version V1.0
 **/
@Service
public class EmployeeImpl {

    @Autowired
    private EmployeeMapper employeeMapper;

    public int saveEmployee(Map<String,Object> map){
        return employeeMapper.insertEmployee(map);
    }

    public int modifyEmployee(Map<String,Object> map){
        return employeeMapper.updateEmployee(map);
    }


}
