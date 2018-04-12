package com.faker.netty.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by faker on 18/4/12.
 */
public class MethodMetaData {
    private int paramsConunt;
    private String name;
    private Class returnType;
    private Method method;
    private Object ownerObject;
    private final Map<String, Integer> queryParamIndexMap;
    private final Map<String, Integer> pathParamIndexMap;
    private Class postParamClass;
    private final Map<String, Integer> postFormParmIndexMap;

    public MethodMetaData(String name) {
        this.name = name;
        queryParamIndexMap = new HashMap<String, Integer>();
        pathParamIndexMap = new HashMap<String, Integer>();
        postFormParmIndexMap = new HashMap<String, Integer>();
        postParamClass = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void addQueryParam(String name, Integer index) {
        this.queryParamIndexMap.put(name, index);
    }

    public void addPathParam(String name, Integer index) {
        this.pathParamIndexMap.put(name, index);
    }

    public void addFormParam(String name, Integer index) {
        this.postFormParmIndexMap.put(name, index);
    }


    public Class getPostParamClass() {
        return postParamClass;
    }

    public void setPostParamClass(Class postParamClass) {
        this.postParamClass = postParamClass;
    }

    public Map<String, Integer> getQueryParamIndexMap() {
        return queryParamIndexMap;
    }

    public Map<String, Integer> getPathParamIndexMap() {
        return pathParamIndexMap;
    }

    public Map<String, Integer> getPostFormParmIndexMap() {
        return postFormParmIndexMap;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getOwnerObject() {
        return ownerObject;
    }

    public void setOwnerObject(Object ownerObject) {
        this.ownerObject = ownerObject;
    }

    public int getParamsConunt() {
        return paramsConunt;
    }

    public void setParamsConunt(int paramsConunt) {
        this.paramsConunt = paramsConunt;
    }
}