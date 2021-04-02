package com.soft.api.service.his.SC;

import com.soft.api.mapper.his.SC.QueryExmRequestMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @ClassName QueryExmRequestImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/2/22
 * @Version V1.0
 **/
@Service
public class QueryExmRequestImpl {

    @Autowired
    private QueryExmRequestMapper queryExmRequestMapper;

    /**
     * @param paramMap
     * @return
     */
    public ResponseEntity queryExmRequest(Map<String,Object> paramMap){
        try {
            Map requestIdMap = (Map)paramMap.get("ExmRequest");
            Map<String, Object> sqdhMap = queryExmRequestMapper.queryExmRequest(requestIdMap);
            return ResultMessage.success(200,"查询申请单检查状态信息成功",sqdhMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询申请单检查状态信息失败");
        }
    }


}
