package com.faker.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by faker on 18/4/11.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormParam {
    String value();
}
