package com.illegalaccess.tutorials.spring.enable.annotation;

import com.illegalaccess.tutorials.spring.enable.configuration.HelloConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HelloConfiguration.class)
public @interface EnableHelloWithConfiguration {
}
