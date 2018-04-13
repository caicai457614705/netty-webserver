package com.faker.netty.model;

import com.faker.netty.annotation.QueryPOJO;

/**
 * Created by faker on 18/4/13.
 */
public class UserQueryParam {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
