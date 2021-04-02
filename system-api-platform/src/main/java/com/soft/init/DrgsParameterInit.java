package com.soft.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName DrgsParameterInit
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/30
 * @Version V1.0
 **/
@Component
@Order(value = 1)//执行顺序由小到大
public class DrgsParameterInit implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

    }
}
