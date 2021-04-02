package com.soft.api.service.his.zigong;

import com.soft.api.mapper.portal.zigong.QueryHisBusKeyMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QueryHisBusKey
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/11
 * @Version V1.0
 **/
@Service
public class QueryHisBusKey {

    @Autowired
    private QueryHisBusKeyMapper queryHisBusKeyMapper;

    /**
     * 获取HIS库表主键 <br>
     * @param QueryName 传入需要主键的表名称<br>
     * @return ResponseEntity
     */
    public ResponseEntity queryHisBusKey(String QueryName){
        String queryName = QueryName.toUpperCase();
        String[] arrQ = queryName.split("_");
        if(arrQ.length == 1){
            return ResultMessage.error(500,"传入的主键表明有误");
        }
        //封装参数
        Map paramMap = new HashMap<String,Object>();
        paramMap.put("TABLENAME", "GY_IDENTITY_"+ arrQ[0]);
        paramMap.put("BMC", queryName);
        //获取当前值
        Long odqz = queryHisBusKeyMapper.getCommonSequence(paramMap);
        if(odqz == null ){
            return ResultMessage.error(500,"未查询出主键");
        }
        Long DQZ = odqz + 1;
        paramMap.put("DQZ", DQZ);
        //当前值+1更新数据库
        int relInt = queryHisBusKeyMapper.updateCommonSequence(paramMap);
        return ResultMessage.success(200,"", DQZ);
    }

}
