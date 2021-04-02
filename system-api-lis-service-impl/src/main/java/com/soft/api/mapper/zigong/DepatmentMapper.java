package com.soft.api.mapper.zigong;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface DepatmentMapper {

    public int insertDepatment(Map<String,Object> paramMap);

    public int updateDepatment(Map<String,Object> paramMap);
}
