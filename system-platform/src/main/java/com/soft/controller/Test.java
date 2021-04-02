package com.soft.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/22
 * @Version V1.0
 **/
@RestController
public class Test {

    @RequestMapping(value = "/test")
    public String test(){
        return "test";
    }

    public static void main(String[] args) {
        Map paramMap = new HashMap();
        paramMap.put("hello","world");
        System.out.println(paramMap);
    }
}
