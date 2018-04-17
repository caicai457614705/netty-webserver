package com.faker.netty.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;

/**
 * Created by faker on 18/4/16.
 */
public class TempTest {
    public static void main(String[] args) {
        URL url = TempTest.class.getResource("/spring-context1.xml");
        System.out.println(url);
        ApplicationContext applicationContext  = new ClassPathXmlApplicationContext();
    }
}
