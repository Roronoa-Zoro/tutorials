package com.illegalaccess.tutorials.spring.enable.starter;

import com.illegalaccess.tutorials.spring.enable.annotation.EnableHelloWithRegistry;
import com.illegalaccess.tutorials.spring.enable.bean.HelloBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableHelloWithRegistry
@ComponentScan("com.illegalaccess.tutorials.spring.enable.starter")
public class StringEnableStarter4Registry {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(StringEnableStarter4Registry.class).run(args);
        HelloBean helloBean = context.getBean(HelloBean.class);
        System.out.println("ready....." + helloBean.sayHi());
        context.close();
    }
}
