package com.soft.Entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MessageEvent
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/13
 * @Version V1.0
 **/
@Data
public class MessageEvent implements Serializable {

    private String id;

    private String state;

    private String txNo;

}
