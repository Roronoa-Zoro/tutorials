package com.illegalaccess.tutorials.spring.enable.configuration;

import com.illegalaccess.tutorials.spring.enable.bean.HelloBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfiguration {

    @Bean
    public HelloBean helloBean() {
        return new HelloBean();
    }
}
