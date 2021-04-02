package com.soft.Entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 定义达州DRGS上传返回
 * @ClassName ResponseEntity
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
@Data
public class ResponseEntity implements Serializable {

    private int Code;

    private String Msg;

    private Object Result;

    public ResponseEntity(int code,String msg,Object result){
        this.Code = code;
        this.Msg = msg;
        this.Result = result;
    }
}
