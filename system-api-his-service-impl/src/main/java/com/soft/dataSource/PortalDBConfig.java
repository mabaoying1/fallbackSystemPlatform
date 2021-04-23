package com.soft.dataSource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName HISDBConfig
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/28
 * @Version V1.0
 **/
@Component
@Data
@ConfigurationProperties(prefix = "spring.portal.datasource")
public class PortalDBConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private long timeBetweenEvictionRunsMillis;
    private long minEvictableIdleTimeMillis;
    private long maxEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;

    private boolean removeAbandoned;
    private int removeAbandonedTimeout;
    private boolean logAbandoned;
}
