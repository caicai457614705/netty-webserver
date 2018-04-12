package com.faker.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by faker on 18/4/11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpMethod {
    String value();
}
