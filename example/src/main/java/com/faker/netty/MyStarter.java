package com.faker.netty;

import com.faker.netty.core.bootstrap.HttpStarter;

/**
 * Created by faker on 18/4/16.
 */
public class MyStarter {

    public static void main(String[] args) throws Exception {
        HttpStarter httpStarter = new HttpStarter(8082);
        httpStarter.start();
    }
}
