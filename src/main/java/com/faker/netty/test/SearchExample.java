package com.faker.netty.test;

import com.alibaba.fastjson.JSONObject;
import com.faker.netty.annotation.*;
import com.faker.netty.common.AbstractControllerParser;
import com.faker.netty.common.DefaultControllerParser;
import com.faker.netty.model.MethodMetaData;
import com.faker.netty.common.AnnotationSearcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by faker on 18/4/12.
 */
public class SearchExample {

    public static void main(String[] args) {
        List<Class> classList = AnnotationSearcher.listClzByAnnotation(Controller.class);
        Class controller = classList.get(0);
        AbstractControllerParser parser = new DefaultControllerParser();
        parser.parseController(controller);
        MethodMetaData methodMetaData = parser.searchUrl("get/user/login");

        System.out.println(JSONObject.toJSONString(methodMetaData));

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", "wzp");
        paramMap.put("password", "123456");

        Object[] params = new Object[methodMetaData.getParamsConunt()];
        Map<String, Integer> queryMap = methodMetaData.getQueryParamIndexMap();
        for (String key : paramMap.keySet()) {
            Integer index = queryMap.get(key);
            if (index != null) {
                params[index] = key;
            }
        }
        try {
            Object result = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), params);
            String s = (String) result;
            System.out.println(s);
//            Class returnType = methodMetaData.getReturnType();
//            Constructor constructor = returnType.getConstructor(returnType);
//            constructor.newInstance(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
