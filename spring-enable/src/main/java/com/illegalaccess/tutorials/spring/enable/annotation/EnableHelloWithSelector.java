package com.illegalaccess.tutorials.spring.enable.annotation;

import com.illegalaccess.tutorials.spring.enable.selector.HelloSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HelloSelector.class)
public @interface EnableHelloWithSelector {
}
