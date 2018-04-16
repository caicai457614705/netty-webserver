package com.faker.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by faker on 18/4/11.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String name();
}

