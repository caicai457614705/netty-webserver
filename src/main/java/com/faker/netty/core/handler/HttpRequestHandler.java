package com.faker.netty.core.handler;

import com.faker.netty.core.processor.HttpProcessor;
import com.faker.netty.util.HeaderUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + msg + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}

