package com.soft.Entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName LoggerEntity
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/29
 * @Version V1.0
 **/
@Data
public class LoggerEntity {

    //编号
    private Long id;

    //客户端请求IP
    private String clientIp;

    //终端请求路径
    private String uri;

    //终端请求方式，普通请求，ajax请求
    private String type;

    //请求方式 post get
    private String method;

    //请求参数内容
    private String paramData;

    //接口唯一session标识
    private String sessionId;

    //请求时间
    private Timestamp time;

    //接口返回时间
    private String returnTime;

    //接口返回数据
    private String returnData;

    //类名称
    private String className;

    //类方法
    private String classMethod;

    //总耗时
    private String TimeConsuming;

    //返回状态
    private int httpStatus;
}
