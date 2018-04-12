# netty-webserver

## netty-webserver

netty-webserver 是一个基于[netty](https://github.com/netty/netty)开发的一个http web服务器

## 使用方法
#### 1.导入maven依赖
```
    <groupId>com.faker.netty</groupId>
    <artifactId>netty-learn</artifactId>
    <version>1.0.0-SNAPSHOT</version>
```
#### 2.Controller编写规范
```
//基本类似于jaxrs规范
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
```

#### 3. 从引导类启动
```
@Starter
public class BootStarter {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap(8080);
        bootstrap.start();

}
```

作者: [@wangzhipeng](https://github.com/caicai457614705)  
个人博客: [汪星人的博客](https://caicai457614705.github.io/)
知乎: [王豆豆](https://www.zhihu.com/people/wang-dou-dou-81-79)

2018 年 04月 12日 

