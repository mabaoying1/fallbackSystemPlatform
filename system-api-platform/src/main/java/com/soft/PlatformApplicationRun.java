package com.soft;
import ctd.net.rpc.frame.UniqueNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @ClassName PlatformApplicationRun
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/28
 * @Version V1.0
 **/
@SpringBootApplication
@EnableScheduling
@ComponentScan(nameGenerator = UniqueNameGenerator.class,
        basePackages = {"com.soft","orm"})
public class PlatformApplicationRun {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplicationRun.class, args);
        System.out.println(" ......................我佛慈悲......................");
        System.out.println("                       _oo0oo_                      ");
        System.out.println("                      o8888888o                     ");
        System.out.println("                      88\" . \"88                     ");
        System.out.println("                      (| -_- |)                     ");
        System.out.println("                      0\\  =  /0                     ");
        System.out.println("                    ___/‘---’\\___                   ");
        System.out.println("                  .' \\|       |/ '.                 ");
        System.out.println("                 / \\\\|||  :  |||// \\                ");
        System.out.println("                / _||||| -卍-|||||_ \\               ");
        System.out.println("               |   | \\\\\\  -  /// |   |              ");
        System.out.println("               | \\_|  ''\\---/''  |_/ |              ");
        System.out.println("               \\  .-\\__  '-'  ___/-. /              ");
        System.out.println("             ___'. .'  /--.--\\  '. .'___            ");
        System.out.println("          .\"\" ‘<  ‘.___\\_<|>_/___.’ >’ \"\".          ");
        System.out.println("         | | :  ‘- \\‘.;‘\\ _ /’;.’/ - ’ : | |        ");
        System.out.println("         \\  \\ ‘_.   \\_ __\\ /__ _/   .-’ /  /        ");
        System.out.println("     =====‘-.____‘.___ \\_____/___.-’___.-’=====     ");
        System.out.println("                       ‘=---=’                      ");
        System.out.println("                                                    ");
        System.out.println("....................佛祖开光 ,永无BUG...................");

        System.out.println(" (♥◠‿◠)ﾉﾞ EurekaServer 项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " _ __                      __                    _     \n" +
                "  | '_ \\   ___      _ _     / _|   ___     __     | |_   \n" +
                "  | .__/  / -_)    | '_|   |  _|  / -_)   / _|    |  _|  \n" +
                "  |_|__   \\___|   _|_|_   _|_|_   \\___|   \\__|_   _\\__|  \n" +
                "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' ");
    }

    /**
     * 用来初始化项目后加载数据到内存里，防止代码获取时报NULL
     * @return
     */
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
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
