package com.soft.config;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @ClassName WebSecurityConfig
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/30
 * @Version V1.0
 **/
//@Configuration
//@EnableWebSecurity

public class WebSecurityConfig {
//        extends WebSecurityConfigurerAdapter{
//
//    /**
//     * @Param: [http]
//     * @Return: void
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//        //注意：为了可以使用 http://${user}:${password}@${host}:${port}/eureka/ 这种方式登录,所以必须是httpBasic,
//        // 如果是form方式,不能使用url格式登录
////        http.csrf().disable()
////                .authorizeRequests()
////                .antMatchers("/actuator/**").permitAll()
////                .anyRequest()
////                .authenticated().and().httpBasic();
//        http.csrf().disable();
//        super.configure(http);
//    }
}
