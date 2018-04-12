package com.faker.netty.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.faker.netty.annotation.Controller;
import com.faker.netty.common.AbstractControllerParser;
import com.faker.netty.common.DefaultControllerParser;
import com.faker.netty.model.MethodMetaData;
import com.faker.netty.util.StringUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.AsciiString;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by faker on 18/4/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

    private AbstractControllerParser parser;

    public HttpRequestHandler(Integer parserType) {
        if (parserType == 1) {
            parser = new DefaultControllerParser();
            parser.parseController(Controller.class);
        }
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
//        校验url 合法性
        HttpMethod method = fullHttpRequest.method();
        String url = method.name().toLowerCase() + fullHttpRequest.uri();

        if (method.equals(HttpMethod.GET)) {
            int queryIndex = url.indexOf("?");
            if (queryIndex > 0) {
//                说明是有queryParam的请求
                Map<String, String> paramMap = parseQueryParam(fullHttpRequest);
                url = url.substring(0, queryIndex);
                MethodMetaData methodMetaData = parser.searchUrl(url);
                if (methodMetaData != null) {
                    Object[] queryParams = new Object[methodMetaData.getParamsConunt()];
                    Map<String, Integer> queryMap = methodMetaData.getQueryParamIndexMap();
                    for (String key : paramMap.keySet()) {
                        Integer index = queryMap.get(key);
                        if (index != null) {
                            queryParams[index] = key;
                        }
                    }
                    Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), queryParams);
                    processResult(object, channelHandlerContext);
                }
            } else {
//                说明是有pathParam的请求
                MethodMetaData methodMetaData = parser.searchUrl(url);
                if (methodMetaData != null) {
                    Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject());
                    processResult(object, channelHandlerContext);
// 说明没有path参数直接调
                } else {
                    int index = url.lastIndexOf("/");
                    MethodMetaData queryRequestMetaData = parser.searchUrl(url.substring(0, index));
                    if (queryRequestMetaData != null) {
                        Map<String, Integer> pathMap = queryRequestMetaData.getPathParamIndexMap();
                        if (pathMap.isEmpty()) {
//                          少参数,抛出异常
                        } else {
                            String pathParam = url.substring(index + 1);
                            Object object = queryRequestMetaData.getMethod().invoke(queryRequestMetaData.getOwnerObject(), pathParam);
                            processResult(object, channelHandlerContext);
                        }
                    }
                }
            }

        } else if (method.equals(HttpMethod.POST)) {
            String contentType = fullHttpRequest.headers().get(CONTENT_TYPE);
            MethodMetaData methodMetaData = parser.searchUrl(url);

            if (contentType.equals("application/json")) {
                String jsonContent = fullHttpRequest.content().toString(Charset.defaultCharset());
                Class pojoClass = methodMetaData.getPojoParamClass();
                Object jsonParam = JSONObject.toJavaObject(JSON.parseObject(jsonContent), pojoClass);
                Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), jsonParam);
                processResult(object, channelHandlerContext);

            } else if (contentType.equals("application/x-www-form-urlencoded")) {
                Map<String, String> paramMap = parseFormParam(fullHttpRequest);
                if (paramMap != null) {
//                说明是有queryParam的请求
                    if (methodMetaData != null) {
                        Object[] formParams = new Object[methodMetaData.getParamsConunt()];
                        Map<String, Integer> formMap = methodMetaData.getFormParamIndexMap();
                        for (String key : paramMap.keySet()) {
                            Integer index = formMap.get(key);
                            if (index != null) {
                                formParams[index] = key;
                            }
                        }
                        Object object = methodMetaData.getMethod().invoke(methodMetaData.getOwnerObject(), formParams);
                        processResult(object, channelHandlerContext);
                    }
                }
            }
        }else {
            throw new RuntimeException();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private String getParam(String json) {

        JSONObject jsonObject = JSONObject.parseObject(json);

        for (Map.Entry entry : jsonObject.entrySet())
            System.out.println(entry.getKey() + ":" + entry.getValue());

        return json;
    }


    private String processJsonRequest() {
        return null;
    }

    private String processQueryAndFormRequest() {
        return null;
    }

    private String processPathRequest() {
        return null;
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
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
    }

}

