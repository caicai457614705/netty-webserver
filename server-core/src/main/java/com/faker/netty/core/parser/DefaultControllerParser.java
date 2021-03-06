package com.faker.netty.core.parser;

import com.faker.netty.annotation.*;
import com.faker.netty.core.common.AnnotationScanner;
import com.faker.netty.model.MethodMetaData;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by faker on 18/4/12.
 */
public class DefaultControllerParser extends AbstractControllerParser {

    public DefaultControllerParser() {
        parseController();
    }

    @Override
    public void parseController() {
        List<Class> parseTargetList = AnnotationScanner.listClzByAnnotation(Controller.class);
        for (Class targetClass : parseTargetList) {
            String basePath = this.parseClass(targetClass);
            //        解析方法
            Method[] methods = targetClass.getDeclaredMethods();
            Object object = null;
            try {
                object = targetClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Method method : methods) {
//            获取方法名字,构造metadata
                MethodMetaData methodMetaData = new MethodMetaData(method.getName());
                methodMetaData.setOwnerObject(object);
                methodMetaData.setMethod(method);
                methodMetaData.setReturnType(method.getReturnType());
                String url = this.parseMethodUrl(method, basePath);
                this.parseParam(method, methodMetaData);
                urlMap.put(url, methodMetaData);
            }
        }

    }

}
