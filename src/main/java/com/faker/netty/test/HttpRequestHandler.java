package com.faker.netty.test;

import com.faker.netty.common.HttpProcessor;
import com.faker.netty.util.HeaderUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Created by faker on 18/4/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private HttpProcessor httpProcessor;

    public HttpRequestHandler(Integer parserType) {
        httpProcessor = new HttpProcessor(parserType);
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //TODO:校验url 合法性
        HttpMethod method = fullHttpRequest.method();
        String url = method.name().toLowerCase() + fullHttpRequest.uri();

        if (method.equals(HttpMethod.GET)) {
            int queryIndex = url.indexOf("?");
            if (queryIndex > 0) {
                url = url.substring(0, queryIndex);
                httpProcessor.processQueryRequest(url, fullHttpRequest, channelHandlerContext);
            } else {
                httpProcessor.processPathRequest(url, channelHandlerContext);
            }
        } else if (method.equals(HttpMethod.POST)) {
            String contentType = fullHttpRequest.headers().get(HeaderUtil.CONTENT_TYPE);
            if (contentType.equals("application/json")) {
                httpProcessor.processJsonRequest(url, fullHttpRequest, channelHandlerContext);
            } else if (contentType.equals("application/x-www-form-urlencoded")) {
                httpProcessor.processFormRequest(url, fullHttpRequest, channelHandlerContext);
            }
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}

