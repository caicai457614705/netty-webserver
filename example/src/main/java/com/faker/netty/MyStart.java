package com.faker.netty;

import com.faker.netty.core.bootstrap.HttpStarter;

/**
 * Created by faker on 18/4/16.
 */
public class MyStart {

    public static void main(String[] args) throws Exception {
        HttpStarter httpStarter = new HttpStarter();
        httpStarter.start();
    }
}