package com.yyl.gateshield.session;

import com.yyl.gateshield.session.handlers.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 这个类的主要作用是初始化新创建的 SocketChannel。
 * 在 initChannel 方法中，首先获取了该 SocketChannel 的 ChannelPipeline，然后向这个 pipeline 中添加了一系列的 ChannelHandler 来处理该 channel 的数据流。
 * HttpRequestDecoder: 这个 handler 负责将字节流解码为 HTTP 请求对象。
 * HttpResponseEncoder: 这个 handler 负责将 HTTP 响应对象编码为字节流。
 * HttpObjectAggregator: 这个 handler 用于将 HTTP 消息的多个部分合并成完整的 FullHttpRequest 或 FullHttpResponse，其中的参数 1024*1024 表示最大合并内容的长度为 1MB。
 * SessionServerHandler: 最后一个 handler 是自定义的 SessionServerHandler，它应用了业务逻辑来处理请求并与客户端进行会话。
 * 这个 SessionChannelInitializer 被设计用于初始化每个新创建的 SocketChannel 的管道，确保它们拥有正确的处理器以处理特定类型的数据流。
 * 通常, 会在 Netty 服务器端的启动过程中使用类似的初始化器来设置管道以正确处理传入的请求。
 */
public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        pipeline.addLast(new SessionServerHandler());
    }
}
