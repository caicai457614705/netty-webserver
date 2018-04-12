package com.faker.netty.test;

import com.alibaba.fastjson.JSONObject;
import com.faker.netty.annotation.Controller;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.FutureListener;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by faker on 18/4/11.
 */
public class HttpPostJsonHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final byte[] hello = {'h', 'e', 'l', 'l', 'o'};

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        fullHttpRequest.uri();
        fullHttpRequest.method();


        String resContent = null;
        String contentType = fullHttpRequest.headers().get(CONTENT_TYPE);
        if (contentType.equals("application/json")) {
            System.out.println("这是个json请求");
            String jsonContent = fullHttpRequest.content().toString(Charset.defaultCharset());
            resContent = getParam(jsonContent);
        } else {
            System.out.println("content-type is not json");
        }


        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(resContent.getBytes("utf-8")));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);

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

}

