package com.soft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaServer
public class EurekaApp {
	//@EnableEurekaServer 表示开启EurekaServer 开启注册中心
	public static void main(String[] args) {
		SpringApplication.run(EurekaApp.class, args);
		System.out.println(" (♥◠‿◠)ﾉﾞ EurekaServer 项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
				" _ __                      __                    _     \n" +
				"  | '_ \\   ___      _ _     / _|   ___     __     | |_   \n" +
				"  | .__/  / -_)    | '_|   |  _|  / -_)   / _|    |  _|  \n" +
				"  |_|__   \\___|   _|_|_   _|_|_   \\___|   \\__|_   _\\__|  \n" +
				"_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
				"\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' ");
	}
}
