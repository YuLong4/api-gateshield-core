package com.yyl.gateshield.session.handlers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yyl.gateshield.bind.IGenericReference;
import com.yyl.gateshield.session.BaseHandler;
import com.yyl.gateshield.session.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 在 session 方法中，处理了收到的 FullHttpRequest 对象。这个方法的逻辑包括：
 * 使用日志记录器 LoggerFactory.getLogger(SessionServerHandler.class) 记录了接收到的请求的 URI 和 HTTP 方法。
 * 创建了一个 DefaultFullHttpResponse 对象作为响应，并设置了响应内容为指定的 JSON 数据。
 * 设置了响应的头部信息，包括内容类型、内容长度、连接保持等 HTTP 头部属性。
 * 随后使用 channel.writeAndFlush(response) 将响应写入到通道并刷新到远程对等体（客户端）。
 */
public class SessionServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(SessionServerHandler.class);

    private final Configuration configuration;

    public SessionServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, final Channel channel, FullHttpRequest request) {
        logger.info("网关接收到请求 uri: {}, method: {}", request.uri(), request.method());

        //返回信息控制
        String methodName = request.uri().substring(1);
        if(methodName.equals("favicon.ico")){
            return;
        }

        //配置返回信息response
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        //服务泛化调用
        IGenericReference reference = configuration.getGenericReference("sayHi");
        String result = reference.$invoke("test") + " " + System.currentTimeMillis();

        //设置回写数据
        response.content().writeBytes(JSON.toJSONBytes(result + request.uri(), SerializerFeature.PrettyFormat));

        //头部信息设置
        HttpHeaders headers = response.headers();
        headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        //配置跨域访问
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        channel.writeAndFlush(response);
    }
}
