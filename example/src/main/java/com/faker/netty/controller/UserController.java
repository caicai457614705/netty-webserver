package com.faker.netty.controller;

import com.faker.netty.UserService;
import com.faker.netty.annotation.*;
import com.faker.netty.model.UserInfo;
import com.faker.netty.model.UserQueryParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by faker on 18/4/11.
 */
@Controller(name = "myController")
@Path("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Get
    @Path("/login")
    public String login(@QueryParam(value = "username") String username, @QueryParam(value = "password") String password) {
        System.out.println("用户名:" + username);
        System.out.println("密码 :" + password);
        userService.say();
        return "success";
    }

    @Get
    @Path("/query")
    public String login(@QueryPOJO UserQueryParam userQueryParam) {
        System.out.println("用户名:" + userQueryParam.getName());
        System.out.println("年龄 :" + userQueryParam.getAge());
        return "success";
    }

    @Post
    @Path("/update")
    public String updateInfo(UserInfo userInfo) {

        return null;
    }

    @Get
    @Path("/get")
    public String getInfo(@PathParam(value = "username") String username) {
        return username;
    }

    @Post
    @Path("/regist")
    public Integer regist(@FormParam(value = "age") Integer age) {
        return age;
    }

}
