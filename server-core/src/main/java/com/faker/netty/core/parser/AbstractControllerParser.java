package com.faker.netty.core.parser;

import com.faker.netty.annotation.*;
import com.faker.netty.exceptions.UrlNotFoundException;
import com.faker.netty.model.MethodMetaData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by faker on 18/4/12.
 */
public abstract class AbstractControllerParser implements ControllerParser {

    protected final Map<String, MethodMetaData> urlMap = new HashMap<>();

    public MethodMetaData searchUrl(String method, String url) {
        String methodUrl = method + url;
        MethodMetaData methodMetaData = urlMap.get(methodUrl);
        if (methodMetaData != null) {
            return methodMetaData;
        } else {
            throw new UrlNotFoundException(url, method);
        }
    }

    public abstract void parseController();

    @Override
    public String parseClass(Class clz) {
        String basePath = "";
        Annotation[] annotations = clz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Path) {
                Path pathParam = (Path) annotation;
                basePath = pathParam.value();
            }
        }
        return basePath;
    }

    @Override
    public String parseMethodUrl(Method method, String basePath) {
//            解析方法路径
        String url = "";
        Annotation[] methodAnnotations = method.getDeclaredAnnotations();
        String httpMethodPath = "";
        String methodPath = "";
        for (Annotation methodAnnotation : methodAnnotations) {

            if (methodAnnotation instanceof Path) {
                Path pathParam = (Path) methodAnnotation;
                methodPath = pathParam.value();
            }
            if (methodAnnotation instanceof Get) {
                httpMethodPath = "get";
            }
            if (methodAnnotation instanceof Post) {
                httpMethodPath = "post";
            }
        }
        url = httpMethodPath + basePath + methodPath;
        return url;
    }

    @Override
    public void parseParam(Method method, MethodMetaData methodMetaData) {
//            解析方法参数
        Parameter[] parameters = method.getParameters();
        int paramLength = parameters.length;
        methodMetaData.setParamsConunt(paramLength);
        for (int i = 0; i < paramLength; i++) {
            Parameter parameter = parameters[i];
            Annotation[] paramAnnotations = parameter.getAnnotations();
            if (paramLength == 1 && paramAnnotations.length == 0) {
                methodMetaData.setPojoParamClass(parameter.getType());
            } else if (paramLength == 1 && parameter.isAnnotationPresent(QueryPOJO.class)) {

//                好像不需要解析field,所以下面都注释掉了
                methodMetaData.setPojoParamClass(parameter.getType());

//                Field[] fields = parameter.getType().getDeclaredFields();
//                for (Field field : fields) {
//                    String fieldName = field.getName();
//                    methodMetaData.addQueryParam(fieldName, -1, field.getType());
//                }
            } else if (paramLength == 1 && parameter.isAnnotationPresent(FormPOJO.class)) {
                methodMetaData.setPojoParamClass(parameter.getType());

//                Field[] fields = parameter.getType().getDeclaredFields();
//                for (Field field : fields) {
//                    String fieldName = field.getName();
//                    methodMetaData.addFormParam(fieldName, -1, field.getType());
//                }
            } else {
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation instanceof PathParam) {
                        PathParam pathParam = (PathParam) paramAnnotation;
                        methodMetaData.addPathParam(pathParam.value(), i, parameter.getType());
                    } else if (paramAnnotation instanceof QueryParam) {
                        QueryParam queryParam = (QueryParam) paramAnnotation;
                        methodMetaData.addQueryParam(queryParam.value(), i, parameter.getType());
                    } else if (paramAnnotation instanceof FormParam) {
                        FormParam formParam = (FormParam) paramAnnotation;
                        methodMetaData.addFormParam(formParam.value(), i, parameter.getType());
                    }
                }
            }

        }
    }

    public Map<String, MethodMetaData> getUrlMap() {
        return urlMap;
    }
}
