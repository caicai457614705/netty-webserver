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
    private String ownerClassName;
    private Object ownerObject;
    private final Map<String, ParamMetaData> queryParamMap;
    private final Map<String, ParamMetaData> pathParamMap;
    private final Map<String, ParamMetaData> formParamMap;
    private Class pojoParamClass;

    public MethodMetaData(String name) {
        this.name = name;
        queryParamMap = new HashMap<String, ParamMetaData>();
        pathParamMap = new HashMap<String, ParamMetaData>();
        formParamMap = new HashMap<String, ParamMetaData>();
        pojoParamClass = null;
    }

    public void setOwnerClassName(String ownerClassName) {
        this.ownerClassName = ownerClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerClassName() {
        return ownerClassName;
    }

    public void addQueryParam(String name, Integer index, Class clz) {
        ParamMetaData paramMetaData = new ParamMetaData(name, index, clz);
        queryParamMap.put(name, paramMetaData);

    }

    public void addFormParam(String name, Integer index, Class clz) {
        ParamMetaData paramMetaData = new ParamMetaData(name, index, clz);
        formParamMap.put(name, paramMetaData);

    }


    public void addPathParam(String name, Integer index, Class clz) {
        ParamMetaData paramMetaData = new ParamMetaData(name, index, clz);
        pathParamMap.put(name, paramMetaData);
    }


    public Map<String, ParamMetaData> getQueryParamMap() {
        return queryParamMap;
    }

    public Map<String, ParamMetaData> getPathParamMap() {
        return pathParamMap;
    }

    public Map<String, ParamMetaData> getFormParamMap() {
        return formParamMap;
    }

    public Class getPojoParamClass() {
        return pojoParamClass;
    }

    public void setPojoParamClass(Class pojoParamClass) {
        this.pojoParamClass = pojoParamClass;
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
