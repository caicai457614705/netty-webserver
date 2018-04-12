package com.faker.netty.common;

import com.faker.netty.annotation.HttpMethod;
import com.faker.netty.annotation.Path;
import com.faker.netty.annotation.PathParam;
import com.faker.netty.annotation.QueryParam;
import com.faker.netty.model.MethodMetaData;

import java.lang.annotation.Annotation;
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
            if (methodAnnotation instanceof HttpMethod) {
                HttpMethod pathParam = (HttpMethod) methodAnnotation;
                httpMethodPath = pathParam.value();
            }
        }
        url = httpMethodPath + basePath + methodPath;
        return url;
    }

    @Override
    public void parseParam(Method method, MethodMetaData methodMetaData) {
//            解析方法参数
        Parameter[] parameters = method.getParameters();
        methodMetaData.setParamsConunt(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation[] paramAnnotations = parameter.getAnnotations();
            if (paramAnnotations.length == 0) {
                methodMetaData.setPostParamClass(parameter.getType());
            } else {
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation instanceof PathParam) {
                        PathParam pathParam = (PathParam) paramAnnotation;
                        methodMetaData.addPathParam(pathParam.value(), i);
                    } else if (paramAnnotation instanceof QueryParam) {
                        QueryParam queryParam = (QueryParam) paramAnnotation;
                        methodMetaData.addQueryParam(queryParam.value(), i);
                    }
                }
            }

        }
    }
}
