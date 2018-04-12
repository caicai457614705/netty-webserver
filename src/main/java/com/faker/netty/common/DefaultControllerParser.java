package com.faker.netty.common;

import com.faker.netty.annotation.*;
import com.faker.netty.model.MethodMetaData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by faker on 18/4/12.
 */
public class DefaultControllerParser extends AbstractControllerParser {

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
                Field[] fields = parameter.getType().getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (field.isAnnotationPresent(QueryParam.class)) {
                        methodMetaData.addQueryParam(fieldName, -1);
                    }
                    if (field.isAnnotationPresent(FormParam.class)) {
                        methodMetaData.addFormParam(fieldName, -1);
                    }
                }
            } else {
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation instanceof PathParam) {
                        PathParam pathParam = (PathParam) paramAnnotation;
                        methodMetaData.addPathParam(pathParam.value(), i);
                    } else if (paramAnnotation instanceof QueryParam) {
                        QueryParam queryParam = (QueryParam) paramAnnotation;
                        methodMetaData.addQueryParam(queryParam.value(), i);
                    } else if (paramAnnotation instanceof FormParam) {
                        FormParam formParam = (FormParam) paramAnnotation;
                        methodMetaData.addFormParam(formParam.value(), i);
                    }
                }
            }

        }
    }
}
