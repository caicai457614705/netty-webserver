package com.faker.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by faker on 18/4/13.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryPOJO {
}