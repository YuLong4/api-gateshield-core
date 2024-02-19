package com.yyl.gateshield.core.socket.handlers;


import com.yyl.gateshield.core.mapping.HttpStatement;
import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.socket.BaseHandler;
import com.yyl.gateshield.core.socket.agreement.AgreementConstants;
import com.yyl.gateshield.core.socket.agreement.GatewayResultMessage;
import com.yyl.gateshield.core.socket.agreement.RequestParser;
import com.yyl.gateshield.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 网络协议处理器
 */
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final Configuration configuration;

    public GatewayServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, final Channel channel, FullHttpRequest request) {
        logger.info("网关接收到请求[全局] uri: {}, method: {}", request.uri(), request.method());

        try {
            // 1. 解析请求参数
            RequestParser requestParser = new RequestParser(request);
            String uri = requestParser.getUri();

            // 2. 保存信息
            HttpStatement httpStatement = configuration.getHttpStatement(uri);
            channel.attr(AgreementConstants.HTTP_STATEMENT).set(httpStatement);

            // 3. 放行服务
            request.retain();
            ctx.fireChannelRead(request);
        } catch (Exception e){
            // 4. 封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._500.getCode(), "网关协议调用失败!" + e.getMessage()));
            channel.writeAndFlush(response);
        }

    }
}
