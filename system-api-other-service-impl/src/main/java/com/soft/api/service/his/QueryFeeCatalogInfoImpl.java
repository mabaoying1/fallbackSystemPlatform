package com.soft.api.service.his;

import com.soft.api.mapper.his.SC.QueryFeeCatalogInfoMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryFeeCatalogInfoImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/3/22
 * @Version V1.0
 **/
@Service
public class QueryFeeCatalogInfoImpl {

    @Autowired
    private QueryFeeCatalogInfoMapper queryFeeCatalogInfoMapper;

    public ResponseEntity queryFeeCatalog(Map<String,Object> map){
        try {
            Map catalogMap = (Map)map.get("Catalog");
            boolean bool = false;
            if(catalogMap.get("fygb") != null && !"".equals(catalogMap.get("fygb").toString())){
                bool = true;
            }
            if(!bool){
                return ResultMessage.error(400,"参数【fygb】不能全部为空");
            }
            List<Map> resultList = queryFeeCatalogInfoMapper.getFeeCatalog(catalogMap);
            return ResultMessage.success(200,"查询费用项目信息成功",resultList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMessage.error(500,"查询费用项目信息失败");
        }
    }
}
