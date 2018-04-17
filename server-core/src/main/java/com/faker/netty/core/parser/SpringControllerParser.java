package com.faker.netty.core.parser;

import com.faker.netty.annotation.Controller;
import com.faker.netty.core.common.AnnotationScanner;
import com.faker.netty.model.MethodMetaData;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by faker on 18/4/17.
 */
public class SpringControllerParser extends AbstractControllerParser {

    private final ApplicationContext ctx;

    private List<Class> controllerList;

    public SpringControllerParser(ApplicationContext ctx) {
        parseController();
        this.ctx = ctx;
    }

    //    解析spring,从容器中获取对象实例
    @Override
    public void parseController() {
        List<Class> parseTargetList = AnnotationScanner.listClzByAnnotation(Controller.class);
        this.controllerList = parseTargetList;
        for (Class targetClass : parseTargetList) {
            String basePath = this.parseClass(targetClass);
            //        解析方法
            Method[] methods = targetClass.getDeclaredMethods();

            for (Method method : methods) {
//            获取方法名字,构造metadata
                MethodMetaData methodMetaData = new MethodMetaData(method.getName());
                methodMetaData.setMethod(method);
                methodMetaData.setOwnerClassName(targetClass.getSimpleName());
                methodMetaData.setReturnType(method.getReturnType());
                String url = this.parseMethodUrl(method, basePath);
                this.parseParam(method, methodMetaData);
                urlMap.put(url, methodMetaData);
            }
        }

    }

    public List<Class> getControllerList() {
        return controllerList;
    }

    public ApplicationContext getCtx() {
        return ctx;
    }
}
