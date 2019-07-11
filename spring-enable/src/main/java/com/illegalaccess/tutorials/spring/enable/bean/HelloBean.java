package com.illegalaccess.tutorials.spring.enable.bean;

import com.illegalaccess.tutorials.spring.enable.annotation.HelloBeanAnnotation;

@HelloBeanAnnotation
public class HelloBean {

    public String sayHi() {
        return "hi there!!!";
    }
}
