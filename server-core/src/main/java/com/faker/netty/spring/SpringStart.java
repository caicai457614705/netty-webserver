package com.faker.netty.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by faker on 18/4/16.
 */
public class SpringStart {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        SpringService springService = (SpringService) applicationContext.getBean("springService");
        springService.say();
    }
}
