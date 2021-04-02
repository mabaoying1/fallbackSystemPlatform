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
    public IZIGONGLISInterface lisWebService() {
        return new ZIGONGLISInterfaceImpl();
    }

    @Bean
    public ISHENGGUKELISInterface lisSGKWebService() {
        return new SHENGGUKELISInterfaceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), lisWebService());
        endpoint.publish("/lis");
        return endpoint;
    }

    @Bean
    public Endpoint endpoint1() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), lisSGKWebService());
        endpoint.publish("/lisShengGuKe");
        return endpoint;
    }


}
