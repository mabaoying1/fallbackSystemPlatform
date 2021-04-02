package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IQuerySfxmMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 查询收费项目
 * <BSXml></BSXml>
 * @ClassName QuerySfxmImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/8
 * @Version V1.0
 **/
@Service
public class QuerySfxmImpl {

    @Autowired
    private IQuerySfxmMapper querySfxmMapper;

    public ResponseEntity querySfxm(Map map) throws Exception {
        List<Map<String, Object>> sfxmList  = querySfxmMapper.getSFXM(map);
        return ResultMessage.success(200,"查询医疗收费项目成功", sfxmList);
    }
}
