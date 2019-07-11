package com.illegalaccess.tutorials.spring.enable.selector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.tutorials.spring.enable.bean.HelloBean;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println("importingClassMetadata..." + om.writeValueAsString(importingClassMetadata));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new String[]{
            HelloBean.class.getName()};
    }
}
