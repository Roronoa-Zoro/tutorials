package com.illegalaccess.tutorials.spring.enable.starter;

import com.illegalaccess.tutorials.spring.enable.annotation.EnableHelloWithConfiguration;
import com.illegalaccess.tutorials.spring.enable.bean.HelloBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 自动启用bean的方式是通过 @import configuration
 */
@EnableHelloWithConfiguration
@ComponentScan("com.illegalaccess.tutorials.spring.enable.starter")
public class SpringEnableStarter4Configuration {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringEnableStarter4Configuration.class).run(args);
        HelloBean helloBean = context.getBean(HelloBean.class);
        System.out.println("ready....." + helloBean.sayHi());
        context.close();
    }
}
