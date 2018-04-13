package com.faker.netty.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.faker.netty.annotation.Controller;
import com.faker.netty.enums.ParamTypeEnum;
import com.faker.netty.model.MethodMetaData;
import com.faker.netty.model.ParamMetaData;
import com.faker.netty.util.HeaderUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by faker on 18/4/13.
 */
public class HttpProcessor {

    private AbstractControllerParser parser;

    public HttpProcessor(Integer parserType) {
        if (parserType == 1) {
            parser = new DefaultControllerParser();
            parser.parseController(Controller.class);
        }
    }

    public void processJsonRequest(String url, FullHttpRequest fullHttpRequest, ChannelHandlerContext channelHandlerContext) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MethodMetaData methodMetaData = parser.searchUrl(url);

        String jsonContent = fullHttpRequest.content().toString(Charset.defaultCharset());
        Class pojoClass = methodMetaData.getPojoParamClass();
        Object jsonParam = JSONObject.toJavaObject(JSON.parseObject(jsonContent), pojoClass);
        Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), jsonParam);
        processResult(object, channelHandlerContext);
    }

    public void processQueryRequest(String url, FullHttpRequest fullHttpRequest, ChannelHandlerContext channelHandlerContext) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String, String> paramMap = parseQueryParam(fullHttpRequest);
        MethodMetaData methodMetaData = parser.searchUrl(url);
        if (methodMetaData != null) {
            if (methodMetaData.getPojoParamClass() != null) {
                processPojoRequest(methodMetaData, paramMap, channelHandlerContext);
            } else {
                Object[] queryParams = new Object[methodMetaData.getParamsConunt()];
                Map<String, ParamMetaData> queryMap = methodMetaData.getQueryParamMap();
                for (String key : paramMap.keySet()) {
                    ParamMetaData paramMetaData = queryMap.get(key);
                    if (paramMetaData != null) {
                        queryParams[paramMetaData.getIndex()] = convertToType(paramMap.get(key), paramMetaData.getType());
                    }
                }
                Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), queryParams);
                processResult(object, channelHandlerContext);
            }

        }
    }

    public void processFormRequest(String url, FullHttpRequest fullHttpRequest, ChannelHandlerContext channelHandlerContext) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MethodMetaData methodMetaData = parser.searchUrl(url);

        Map<String, String> paramMap = parseFormParam(fullHttpRequest);
        if (paramMap != null) {
//                说明是有queryParam的请求
            if (methodMetaData != null) {
                if (methodMetaData.getPojoParamClass() != null) {
                    processPojoRequest(methodMetaData, paramMap, channelHandlerContext);
                } else {
                    Object[] formParams = new Object[methodMetaData.getParamsConunt()];
                    Map<String, ParamMetaData> formMap = methodMetaData.getFormParamMap();
                    for (String key : paramMap.keySet()) {
                        ParamMetaData paramMetaData = formMap.get(key);
                        if (paramMetaData != null) {
                            formParams[paramMetaData.getIndex()] = convertToType(paramMap.get(key), paramMetaData.getType());
                        }
                    }
                    Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), formParams);
                    processResult(object, channelHandlerContext);
                }
            }
        }
    }

    public void processPathRequest(String url, ChannelHandlerContext channelHandlerContext) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MethodMetaData methodMetaData = parser.searchUrl(url);
        if (methodMetaData != null) {
            Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject());
            processResult(object, channelHandlerContext);
        } else {
            int index = url.lastIndexOf("/");
            MethodMetaData queryRequestMetaData = parser.searchUrl(url.substring(0, index));
            if (queryRequestMetaData != null) {
                Map<String, ParamMetaData> pathMap = queryRequestMetaData.getPathParamMap();
                if (pathMap.isEmpty()) {
//TODO:少参数抛出异常
                } else {
                    String pathParam = url.substring(index + 1);
                    String firstKey = pathMap.keySet().iterator().next();
                    ParamMetaData paramMetaData = pathMap.get(firstKey);
                    Object object = queryRequestMetaData.getMethod().invoke(queryRequestMetaData.getOwnerObject(), convertToType(pathParam, paramMetaData.getType()));
                    processResult(object, channelHandlerContext);
                }
            }
        }
    }

    private void processPojoRequest(MethodMetaData methodMetaData, Map<String, String> paramMap, ChannelHandlerContext channelHandlerContext) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object paramInstance = methodMetaData.getPojoParamClass().newInstance();
        for (Field field : paramInstance.getClass().getDeclaredFields()) {
            String param = paramMap.get(field.getName());
            if (param != null) {
                field.setAccessible(true);
                field.set(paramInstance, convertToType(param, field.getType()));
            }
        }
        Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), paramInstance);
        processResult(object, channelHandlerContext);
    }


    private Map<String, String> parseQueryParam(FullHttpRequest fullHttpRequest) {
        Map<String, String> paramMap = new HashMap<>();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
        if (queryStringDecoder.parameters().size() > 0) {

            queryStringDecoder.parameters().entrySet().forEach(entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                paramMap.put(entry.getKey(), entry.getValue().get(0));
            });
            return paramMap;

        } else {
            return null;
        }

    }

    private Map<String, String> parseFormParam(FullHttpRequest fullHttpRequest) {
        Map<String, String> paramMap = new HashMap<>();
        // 是POST请求
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
        decoder.offer(fullHttpRequest);

        List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
        for (InterfaceHttpData parm : parmList) {
            Attribute data = (Attribute) parm;
            try {
                paramMap.put(data.getName(), data.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return paramMap;
    }

    private void processResult(Object object, ChannelHandlerContext channelHandlerContext) throws IllegalAccessException, InstantiationException {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(JSON.toJSONString(object).getBytes(Charset.defaultCharset())));
        response.headers().set(HeaderUtil.CONTENT_TYPE, "application/json");
        response.headers().setInt(HeaderUtil.CONTENT_LENGTH, response.content().readableBytes());
        channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
    }


    private <T> T convertToType(String value, Class<T> clz) {
        //TODO:解析各种类型参数
        if (clz.getName().contains("java.lang")) {
            Class[] classes = new Class[]{String.class};
            Constructor constructor = null;
            try {
                constructor = clz.getConstructor(classes);

                return (T) constructor.newInstance(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String clzName = clz.getName();
            switch (clzName) {
                case "java.math.BigDecimal":
                    return (T) new BigDecimal(value);
                case "int":
                    return (T) Integer.valueOf(value);
                case "long":
                    return (T) Long.valueOf(value);
                case "double":
                    return (T) Double.valueOf(value);
                case "float":
                    return (T) Float.valueOf(value);
                case "short":
                    return (T) Short.valueOf(value);
                case "boolean":
                    return (T) Boolean.valueOf(value);
                case "java.util.Date": {
//                    TODO:时间格式解析
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(value);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return (T) date;
                }
            }
        }
//TODO:list类型
        return null;

    }

}
