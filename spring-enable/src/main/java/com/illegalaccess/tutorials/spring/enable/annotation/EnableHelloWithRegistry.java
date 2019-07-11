package com.illegalaccess.tutorials.spring.enable.annotation;


import com.illegalaccess.tutorials.spring.enable.registry.HellobeanRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HellobeanRegistry.class)
public @interface EnableHelloWithRegistry {
}
