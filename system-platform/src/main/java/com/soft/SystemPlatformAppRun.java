package com.soft;

import ctd.net.rpc.frame.UniqueNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @ClassName SystemPlatformAppRun
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/22
 * @Version V1.0
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(nameGenerator = UniqueNameGenerator.class,
        basePackages = {"ctd.net.rpc.aop","ctd.net.rpc.Entity","ctd.net.rpc.util",
                "com.soft"})
public class SystemPlatformAppRun {

    public static void main(String[] args) {
        SpringApplication.run(SystemPlatformAppRun.class, args);
        System.out.println(" (♥◠‿◠)ﾉﾞ system-platform 项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " _ __                      __                    _     \n" +
                "  | '_ \\   ___      _ _     / _|   ___     __     | |_   \n" +
                "  | .__/  / -_)    | '_|   |  _|  / -_)   / _|    |  _|  \n" +
                "  |_|__   \\___|   _|_|_   _|_|_   \\___|   \\__|_   _\\__|  \n" +
                "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' ");
    }


//    /**
//     * 是因为配置cxf接口
//     * springboot默认注册的是 dispatcherServlet，
//     * 当手动配置 ServletRegistrationBean后springboot不会再去注册默认的dispatcherServlet，
//     * 所以需要我们在启动类里手动去注册一个dispatcherServlet，
//     * @return
//     */
//    @Bean
//    public ServletRegistrationBean restServlet() {
//        //注解扫描上下文
//        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
//        //项目包名
//        applicationContext.scan("com.soft.controller.**"
//        );
//        applicationContext.setBeanNameGenerator(new UniqueNameGenerator());
//        DispatcherServlet rest_dispatcherServlet = new DispatcherServlet(applicationContext);
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean<>(rest_dispatcherServlet);
//        registrationBean.setLoadOnStartup(1);
//        registrationBean.addUrlMappings("/*");
//        return registrationBean;
//    }


}
