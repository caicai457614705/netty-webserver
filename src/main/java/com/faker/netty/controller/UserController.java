package com.faker.netty.controller;

import com.faker.netty.annotation.Controller;
import com.faker.netty.annotation.HttpMethod;
import com.faker.netty.annotation.Path;
import com.faker.netty.annotation.QueryParam;
import com.faker.netty.model.UserInfo;

/**
 * Created by faker on 18/4/11.
 */
@Controller(name = "myController")
@Path("/user")
public class UserController {

    @HttpMethod(value = "get")
    @Path("/login")
    public String login(@QueryParam(value = "username") String username, @QueryParam(value = "password") String password) {
        System.out.println("用户名:" + username);
        System.out.println("密码 :" + password);
        return "success";
    }

    @HttpMethod(value = "post")
    @Path("/update")
    public String updateInfo(UserInfo userInfo) {
        return null;
    }

}
