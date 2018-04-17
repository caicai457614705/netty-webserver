package com.faker.netty.core.parser;

import com.faker.netty.model.MethodMetaData;

import java.lang.reflect.Method;

/**
 * Created by faker on 18/4/17.
 */
public interface ControllerParser {

    MethodMetaData searchUrl(String method, String url);

    void parseController();

    String parseClass(Class clz);

    String parseMethodUrl(Method method, String basePath);

    void parseParam(Method method, MethodMetaData methodMetaData);
}

