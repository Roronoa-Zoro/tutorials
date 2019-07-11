package com.illegalaccess.tutorials.spring.enable.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illegalaccess.tutorials.spring.enable.annotation.HelloBeanAnnotation;
import com.illegalaccess.tutorials.spring.enable.bean.HelloBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * 同时可以继承 ResourceLoaderAware, BeanFactoryAware 等获取其他信息
 */
public class HellobeanRegistry implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println("importingClassMetadata..." + om.writeValueAsString(importingClassMetadata));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);

        //方式一 直接使用对应的Bean.class 过滤
        TypeFilter helloServiceFilter = new AssignableTypeFilter(HelloBean.class);
        scanner.addIncludeFilter(helloServiceFilter);

        //方式二  使用注解过滤，即在目标bean上添加特定的注解
//        scanner.addIncludeFilter(new AnnotationTypeFilter(HelloBeanAnnotation.class));

        scanner.scan("com.illegalaccess.tutorials.spring.enable");
    }
}
