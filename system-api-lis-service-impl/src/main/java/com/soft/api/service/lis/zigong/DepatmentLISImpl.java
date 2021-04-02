package com.soft.api.service.lis.zigong;

import com.soft.api.mapper.zigong.DepatmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName DepatmentAddImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/27
 * @Version V1.0
 **/
@Service
public class DepatmentLISImpl {

    @Autowired
    private DepatmentMapper depatmentMapper;

    public int saveDepatment(Map<String,Object> paramMap){
        return depatmentMapper.insertDepatment(paramMap);
    }

    public int modifyDepatment(Map<String,Object> paramMap){
        return depatmentMapper.updateDepatment(paramMap);
    }



}
