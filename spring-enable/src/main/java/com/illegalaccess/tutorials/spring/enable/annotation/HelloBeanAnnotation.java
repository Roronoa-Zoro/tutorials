package com.illegalaccess.tutorials.spring.enable.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HelloBeanAnnotation {
}
