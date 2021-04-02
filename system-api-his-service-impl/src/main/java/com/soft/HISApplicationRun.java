package com.soft;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import ctd.net.rpc.frame.UniqueNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableHystrix
@EnableDistributedTransaction
//@EnableTransactionManagement
@ComponentScan(nameGenerator = UniqueNameGenerator.class,
		basePackages = {"ctd.net.rpc.util",
				"ctd.net.rpc.Entity",
				"com.soft","ctd.net.rpc.aop"})
public class HISApplicationRun {

	public static void main(String[] args) {
		SpringApplication.run(HISApplicationRun.class, args);
		System.out.println(" (♥◠‿◠)ﾉﾞ system-api-his-service-impl 项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
				" _ __                      __                    _     \n" +
				"  | '_ \\   ___      _ _     / _|   ___     __     | |_   \n" +
				"  | .__/  / -_)    | '_|   |  _|  / -_)   / _|    |  _|  \n" +
				"  |_|__   \\___|   _|_|_   _|_|_   \\___|   \\__|_   _\\__|  \n" +
				"_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
				"\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' ");
	}


	/**
	 * 是因为配置cxf接口
	 * springboot默认注册的是 dispatcherServlet，
	 * 当手动配置 ServletRegistrationBean后springboot不会再去注册默认的dispatcherServlet，
	 * 所以需要我们在启动类里手动去注册一个dispatcherServlet，
	 * @return
	 */
	@Bean
	public ServletRegistrationBean restServlet() {
		//注解扫描上下文
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		//项目包名
		applicationContext.scan("com.soft.api.**"
		);
		applicationContext.setBeanNameGenerator(new UniqueNameGenerator());
		DispatcherServlet rest_dispatcherServlet = new DispatcherServlet(applicationContext);
		ServletRegistrationBean registrationBean = new ServletRegistrationBean<>(rest_dispatcherServlet);
		registrationBean.setLoadOnStartup(1);
		registrationBean.addUrlMappings("/*");
		return registrationBean;
	}
}
