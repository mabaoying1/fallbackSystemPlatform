package com.soft.ws;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @ClassName EsbWebServiceConfig
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/23
 * @Version V1.0
 **/
@Configuration
public class EsbWebServiceConfig {

    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/ws-api/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public IZIGONGHISInterface hisZiGongWebService() {
        return new ZIGONGHISInterfaceImpl();
    }

    @Bean
    public ISHENGGUKEHISInterface hisShengGuKeWebService() {
        return new SHENGGUKEHISInterfaceImpl();
    }

    @Bean
    public ISCPublicInterface scPublicInterfaceService(){return new SCPublicInterfaceImpl();}


    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), hisZiGongWebService());
        endpoint.publish("/hisZiGong");
        return endpoint;
    }

    @Bean
    public Endpoint endpoint1() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), hisShengGuKeWebService());
        endpoint.publish("/hisShengGuKe");
        return endpoint;
    }

    @Bean
    public Endpoint endpoint2() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), scPublicInterfaceService());
        endpoint.publish("/sc");
        return endpoint;
    }

}
