package com.soft.framework;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @ClassName WebConfig
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/29
 * @Version V1.0
 **/

/**
 * 类访问拦截器
 * WebMvcConfigurerAdapter 过时了,用WebMvcConfigurationSupport代替
 * WebMvcConfigurationSupport 会导致覆盖静态资源路径，所有需重新加载
 */
@Configuration //成为Springboot的配置类，以下代码生效
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .excludePathPatterns("/static/**");
        super.addInterceptors(registry);
    }

    /**
     * 加载静态资源配置路径
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //重写这个方法，映射静态资源文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }
}
