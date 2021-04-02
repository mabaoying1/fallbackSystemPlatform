package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IQueryMedicalProjectMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 查询HIS医疗项目 <br>
 *  <![CDATA[ <br>
 * 	<BSXml> <br>
 * 	<itemcode>收费项目</itemcode> <br>
 * 	<itemtype>收费项目类型</itemtype> <br>
 * 	<itemname>收费项目名称</itemname> <br>
 * 	</BSXml> <br>
 *  ]]> <br>
 * @ClassName QueryMedicalProjectImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/8
 * @Version V1.0
 **/
@Service
public class QueryMedicalProjectImpl {


    @Autowired
    private IQueryMedicalProjectMapper queryMedicalProjectMapper;


    public ResponseEntity queryMedicalProject(Map map) throws Exception {
        if(map.get("itemtype") == null || "".equals(map.get("itemtype").toString())){
            return ResultMessage.error(500,"【itemtype】未传值");
        }
        List<Map<String, Object>> medicalProjectList  = queryMedicalProjectMapper.getMedicalProject(map);
        return ResultMessage.success(200,"查询医疗目录成功", medicalProjectList);
    }
}
