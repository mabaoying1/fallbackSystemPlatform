package com.soft.Entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DrgsParameterEntity
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/30
 * @Version V1.0
 **/
@Configuration
@ConfigurationProperties(prefix = "drgs")
@Data
public class DrgsParameterEntity {

    private String ipAddr;

    private String ipAddr2;

    private String userId;

    private String siginMethod;

    private String token;

    private String urlAddr;

    private String odinAddr;

    private String odinNameSpace;

    private String odinMethod;

    private String hospitalID;
}
