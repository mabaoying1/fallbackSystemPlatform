package com.soft.util;

import com.soft.Entity.ResponseEntity;

/**
 * @ClassName Result
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
public class ResultMessage {

    public static final int code = 100;

    public static final int errorCode = 500;

    public static final String msg = "成功";

    public static final String errorMsg = "失败";

    public static final Object result = null;


    public static ResponseEntity error(){
        return new ResponseEntity(errorCode , errorMsg , result);
    }

    public static ResponseEntity error(int code){
        return new ResponseEntity(code , errorMsg , result);
    }

    public static ResponseEntity error(String msg){
        return new ResponseEntity(errorCode , msg , result);
    }

    public static ResponseEntity error(int code, String msg){
        return new ResponseEntity(code , msg , result);
    }

    public static ResponseEntity error(int code, String msg, Object result){
        return new ResponseEntity(code , msg , result);
    }


    public static ResponseEntity success(){
        return success(code, msg, result);
    }

    public static ResponseEntity success(int code){
        return success(code, msg, result);
    }

    public static ResponseEntity success(String msg){
        return success(code, msg, result);
    }

    public static ResponseEntity success(int code,String msg){
        return success(code, msg, result);
    }

    public static ResponseEntity success(int code, String msg, Object result){
        return new ResponseEntity(code , msg , result);
    }
}
