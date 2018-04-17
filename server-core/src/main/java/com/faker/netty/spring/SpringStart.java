package com.faker.netty.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by faker on 18/4/16.
 */
public class SpringStart {

    @Autowired
    private SpringService springService;

    private void test() {
        springService.say();
        System.out.println("test");
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        SpringStart springStart = (SpringStart) applicationContext.getBean("springStart");
        springStart.test();
    }
}
