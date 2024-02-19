package com.yyl.gateshield.core.socket.handlers;

import com.yyl.gateshield.core.bind.IGenericReference;
import com.yyl.gateshield.core.executor.result.SessionResult;
import com.yyl.gateshield.core.session.defaults.DefaultGatewaySessionFactory;
import com.yyl.gateshield.core.socket.BaseHandler;
import com.yyl.gateshield.core.socket.agreement.AgreementConstants;
import com.yyl.gateshield.core.socket.agreement.GatewayResultMessage;
import com.yyl.gateshield.core.socket.agreement.RequestParser;
import com.yyl.gateshield.core.socket.agreement.ResponseParser;
import com.yyl.gateshield.core.session.GatewaySession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 协议数据处理
 */
public class ProtocolDataHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(ProtocolDataHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public ProtocolDataHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求[消息] uri：{} method：{}", request.uri(), request.method());
        try {
            // 1. 解析请求参数
            RequestParser requestParser = new RequestParser(request);
            String uri = requestParser.getUri();
            if (uri == null) return;
            Map<String, Object> args = requestParser.parse();

            // 2. 调用会话服务
            GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
            IGenericReference reference = gatewaySession.getMapper();
            SessionResult result = reference.$invoke(args);

            // 3.封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse(result.getCode().equals("0000") ? GatewayResultMessage.buildSuccess(result.getData()) : GatewayResultMessage.buildError(AgreementConstants.ResponseCode._404.getCode(), "网关协议调用失败!"));
            channel.writeAndFlush(response);
        } catch (Exception e) {
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._502.getCode(), "网关协议调用失败！" + e.getMessage()));
            channel.writeAndFlush(response);
        }
    }
}
