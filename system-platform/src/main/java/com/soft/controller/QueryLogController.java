package com.soft.controller;

import ctd.net.rpc.util.ElasticsearchUtil;
import ctd.net.rpc.util.ResponseEntity;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.internal.util.LinkedArrayList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryLogController
 * @Description: TODO
 * @Author ljx
 * @Date 2021/3/1
 * @Version V1.0
 **/
@Controller
public class QueryLogController {

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

//    @Autowired
//    private RestartEndpoint restartEndpoint;

    @RequestMapping(value = "/queryLog")
    @ResponseBody
    public Map<String, Object> queryInfo(@RequestParam Map<String, Object> map) throws IOException, TemplateException, ClassNotFoundException {

        ResponseEntity s2 = null;
        if(map.get("methodName")!=null){

        }
        s2=elasticsearchUtil.queryElasticsearchDataByCondition("query",map);
        s2 = elasticsearchUtil.queryElasticsearchDataAll("query");

        List<Map<String, String>> list = null;
        if (s2.getResult() != null) {
            list = (List<Map<String, String>>) s2.getResult();
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", s2.getCode());
//        layui 返回200不能直接遍历
        if (s2.getCode() == 200) {
            resMap.put("code", 0);
        }
        resMap.put("msg", s2.getMsg());
        resMap.put("count", list.size());
        resMap.put("data", list);
        return resMap;
    }
}
