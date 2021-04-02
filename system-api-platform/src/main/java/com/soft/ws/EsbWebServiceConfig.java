package com.soft.ws;

import com.soft.framework.WSAuthInterceptor;
import com.soft.framework.WebConfig;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
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
    public IPlatformDRGSInterface platDRGSWebService() {
        return new PlatformDRGSInterfaceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        //http://具体IP:8700/ws-api/Drgs?wsdl
        EndpointImpl endpoint = new EndpointImpl(springBus(), platDRGSWebService());
        endpoint.publish("/Drgs");
//        endpoint.getInInterceptors().add(new WSAuthInterceptor());
        return endpoint;
    }



    @Bean
    public IDAZHOUPlatformDRGSInterface idazhouPlatformDRGSInterface() {
        return new DAZHOUPlatformDRGSInterfaceImpl();
    }

    @Bean
    public Endpoint endpoint1() {
        //http://具体IP:8700/ws-api/dazhouDrgs?wsdl
        EndpointImpl endpoint = new EndpointImpl(springBus(), idazhouPlatformDRGSInterface());
        endpoint.publish("/dazhouDrgs");
        return endpoint;
    }
    @Bean
    public JbzyyDiAnInerface jbzyyDiAnInerface() {
        return new JbzyyDiAnImpl();
    }

    @Bean
    public Endpoint endpoint2() {
        //http://具体IP:8700/ws-api/jbzyyzygDiAn?wsdl
        EndpointImpl endpoint = new EndpointImpl(springBus(), jbzyyDiAnInerface());
        endpoint.publish("/jbzyyzygDiAn");
        return endpoint;
    }

}
