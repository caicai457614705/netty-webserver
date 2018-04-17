package com.faker.netty.core.handler;

import com.faker.netty.annotation.Controller;
import com.faker.netty.core.bootstrap.HttpStarterInitializer;
import com.faker.netty.core.common.ControllerRegistryBean;
import com.faker.netty.core.parser.ControllerParser;
import com.faker.netty.core.parser.DefaultControllerParser;
import com.faker.netty.core.parser.SpringControllerParser;
import com.faker.netty.core.processor.HttpProcessor;
import com.faker.netty.util.HeaderUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by faker on 18/4/11.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private HttpProcessor httpProcessor;

    public HttpRequestHandler(ControllerParser parser) {
        httpProcessor = new HttpProcessor(parser, false);
    }

    public HttpRequestHandler(ControllerParser parser, boolean useSpring) {
        httpProcessor = new HttpProcessor(parser, useSpring);
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //TODO:校验url 合法性
        HttpMethod method = fullHttpRequest.method();
        String url = fullHttpRequest.uri();

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
        sendError(ctx, HttpResponseStatus.BAD_REQUEST, cause.getMessage());
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + msg + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static HttpRequestHandler getHttpHandler() {
        HttpRequestHandler requestHandler;

        if (HttpStarterInitializer.class.getResource("/spring-context.xml") != null) {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
            ControllerRegistryBean controllerRegistryBean = (ControllerRegistryBean) applicationContext.getBean("controllerRegistryBean");
            requestHandler = new HttpRequestHandler(controllerRegistryBean.getParser(), true);
        } else {
            DefaultControllerParser defaultControllerParser = new DefaultControllerParser();
            requestHandler = new HttpRequestHandler(defaultControllerParser);
        }
        return requestHandler;
    }

}

