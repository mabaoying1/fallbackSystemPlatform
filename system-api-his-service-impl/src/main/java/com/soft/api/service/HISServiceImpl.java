package com.soft.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.soft.api.feign.LISServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HISServiceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/21
 * @Version V1.0
 **/
@RestController
public class HISServiceImpl implements IHISQueryService {


    @Autowired
    private LISServiceFeign lisServiceFeign;


    @Override
    @RequestMapping("/outPatientRegisteredQuery")
    public String outPatientRegisteredQuery(String param) {
        return "outPatientRegisteredQuery";
    }

    @HystrixCommand(fallbackMethod = "queryToLisFallback")
    @RequestMapping(value = "/queryToLis")
    public String queryToLis(){
         String str = lisServiceFeign.querylis("");
         return str;
    }

    public String queryToLisFallback(){
        return "服务降级熔断";
    }

}
