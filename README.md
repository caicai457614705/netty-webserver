# netty-webserver

## 简介
netty-webserver 是一个基于[netty](https://github.com/netty/netty)开发的一个http web服务器

## 使用方法
#### 1.导入maven依赖
```
    <groupId>com.faker.netty</groupId>
    <artifactId>server-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
```
#### 2.Controller编写规范
- 详情参考example module
```
//基本类似于jaxrs规范
@Controller(name = "userController")
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
```

#### 3. 从引导类启动
```
public class BootStarter {
    public static void main(String[] args) {
        HttpStarter httpStarter = new HttpStarter(8082);
        httpStarter.start();
}
```

#### 4. 对spring的支持
```
1. 检查resources目录下是否包含spring-context.xml配置文件(配置文件中需要开启自动扫描)。
2. 扫描到组件ControllerRegistryBean,该组件会解析Controller向Spring容器动态注册
3. 接受请求后,从applicationContext中getBean,通过反射调用处理请求.
```

#### 5. 待完善功能
1. 对List Query参数的支持, 对返回参数为List的支持。
2. 对Header参数和Cookie参数的支持
3. 提供扩展点用于个性化处理请求
4. 日期格式化处理,提供格式注解(目前只支持yyyy-MM-dd HH:mm:ss)
5. 异常处理,sendError方法完善
6. MediaType支持(目前支持json和form,默认返回json)

作者: [@wangzhipeng](https://github.com/caicai457614705)  
个人博客: [汪星人的博客](https://caicai457614705.github.io/)
知乎: [王豆豆](https://www.zhihu.com/people/wang-dou-dou-81-79)

2018 年 04月 12日 

