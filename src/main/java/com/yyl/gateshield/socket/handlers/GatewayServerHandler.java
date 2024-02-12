package com.yyl.gateshield.socket.handlers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yyl.gateshield.bind.IGenericReference;
import com.yyl.gateshield.session.GatewaySession;
import com.yyl.gateshield.session.defaults.DefaultGatewaySessionFactory;
import com.yyl.gateshield.socket.BaseHandler;
import com.yyl.gateshield.socket.agreement.RequestParser;
import com.yyl.gateshield.socket.agreement.ResponseParse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 网络协议处理器
 */
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, final Channel channel, FullHttpRequest request) {
        logger.info("网关接收到请求 uri: {}, method: {}", request.uri(), request.method());

        // 1. 解析请求参数
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if(uri == null) return;
        Map<String, Object> args = requestParser.parse();

        // 2. 调用会话服务
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        IGenericReference reference = gatewaySession.getMapper();
        Object result = reference.$invoke(args);

        // 3. 封装返回结果
        DefaultFullHttpResponse response = new ResponseParse().parse(result);
        channel.writeAndFlush(response);
    }
}
