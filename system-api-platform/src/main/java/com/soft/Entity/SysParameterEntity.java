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
@ConfigurationProperties(prefix = "system")
@Data
public class SysParameterEntity {

    /**
     * 是否使用 Elasticsearch 存放日志<br>
     */
    private String isElasticsearch;

    /**
     *Elasticsearch 服务器地址<br>
     */
    private String elasticsearchHost;

    /**
     *Elasticsearch 端口号，http 访问端口9200；客户端访问端口9300<br>
     */
    private String elasticsearchPort;

    private String clusterName;
}
