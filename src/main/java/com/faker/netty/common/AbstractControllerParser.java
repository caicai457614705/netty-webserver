package com.faker.netty.common;

import com.faker.netty.annotation.HttpMethod;
import com.faker.netty.annotation.Path;
import com.faker.netty.annotation.PathParam;
import com.faker.netty.annotation.QueryParam;
import com.faker.netty.model.MethodMetaData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by faker on 18/4/12.
 */
public abstract class AbstractControllerParser {

    private final Map<String, MethodMetaData> urlMap = new HashMap<>();

    public MethodMetaData searchUrl(String url) {
        return urlMap.get(url);
    }

    public void parseController(Class clz) {
        String basePath = this.parseClass(clz);
        //        解析方法
        Method[] methods = clz.getDeclaredMethods();
        Object object = null;
        try {
            object = clz.newInstance();
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

    public abstract String parseClass(Class clz);

    public abstract String parseMethodUrl(Method method, String basePath);

    public abstract void parseParam(Method method, MethodMetaData methodMetaData);
}