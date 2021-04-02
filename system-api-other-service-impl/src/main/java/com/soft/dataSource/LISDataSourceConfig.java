package com.soft.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import ctd.net.rpc.frame.UniqueNameGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;


/**
 * @ClassName HISDataSourceConfig
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/28
 * @Version V1.0
 **/
@Configuration
@MapperScan(basePackages = { "com.soft.api.mapper.lis" }, sqlSessionTemplateRef = "sqlSessionTemplateDBLIS",nameGenerator = UniqueNameGenerator.class)
public class LISDataSourceConfig {


    @Bean(name = "dataSourceDBLIS")
    public DataSource dataSourceCar(LISDBConfig dbConfig) throws SQLException {
        DruidDataSource druid = new DruidDataSource();
        // 监控统计拦截的filters
//        druid.setFilters(filters);
        // 配置基本属性
        druid.setDriverClassName(dbConfig.getDriverClassName());
        druid.setUsername(dbConfig.getUsername());
        druid.setPassword(dbConfig.getPassword());
        druid.setUrl(dbConfig.getUrl());
        //初始化时建立物理连接的个数
        druid.setInitialSize(dbConfig.getInitialSize());
        //最大连接池数量
        druid.setMaxActive(dbConfig.getMaxActive());
        //最小连接池数量
        druid.setMinIdle(dbConfig.getMinIdle());
        //获取连接时最大等待时间，单位毫秒。
        druid.setMaxWait(dbConfig.getMaxWait());
        //间隔多久进行一次检测，检测需要关闭的空闲连接
        druid.setTimeBetweenEvictionRunsMillis(dbConfig.getTimeBetweenEvictionRunsMillis());
        //一个连接在池中最小生存的时间
        druid.setMinEvictableIdleTimeMillis(dbConfig.getMinEvictableIdleTimeMillis());
        //用来检测连接是否有效的sql
        druid.setValidationQuery(dbConfig.getValidationQuery());
        //建议配置为true，不影响性能，并且保证安全性。
        druid.setTestWhileIdle(dbConfig.isTestWhileIdle());
        //申请连接时执行validationQuery检测连接是否有效
        druid.setTestOnBorrow(dbConfig.isTestOnBorrow());
        druid.setTestOnReturn(dbConfig.isTestOnReturn());
        //是否缓存preparedStatement，也就是PSCache，oracle设为true，mysql设为false。分库分表较多推荐设置为false
//        druid.setPoolPreparedStatements(poolPreparedStatements);
        // 打开PSCache时，指定每个连接上PSCache的大小
//        druid.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        return druid;
    }


    @Bean(name = "sqlSessionFactoryDBLIS")
    public SqlSessionFactory sqlSessionFactoryCar(@Qualifier("dataSourceDBLIS") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new
                PathMatchingResourcePatternResolver().getResources("classpath:mapper/lis/**/*.xml"));
        return bean.getObject();
    }


    @Bean(name = "sqlSessionTemplateDBLIS")
    public SqlSessionTemplate sqlSessionTemplateCar(
            @Qualifier("sqlSessionFactoryDBLIS") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
