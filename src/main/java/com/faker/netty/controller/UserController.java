package com.faker.netty.controller;

import com.faker.netty.annotation.*;
import com.faker.netty.model.UserInfo;

/**
 * Created by faker on 18/4/11.
 */
@Controller(name = "myController")
@Path("/user")
public class UserController {

    @Get
    @Path("/login")
    public String login(@QueryParam(value = "username") String username, @QueryParam(value = "password") String password) {
        System.out.println("用户名:" + username);
        System.out.println("密码 :" + password);
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
    public Integer regist(@FormParam(value = "age")Integer age) {
        return age;
    }

}
