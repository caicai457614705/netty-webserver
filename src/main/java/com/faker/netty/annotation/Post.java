package com.faker.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by faker on 18/4/12.
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Post {
}
