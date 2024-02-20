package com.yyl.gateshield.core.socket;

import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.session.defaults.DefaultGatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * 网关会话服务
 */
public class GatewaySocketServer implements Callable<Channel> {

    private final Logger logger = LoggerFactory.getLogger(GatewaySocketServer.class);

    private final Configuration configuration;
    private DefaultGatewaySessionFactory gatewaySessionFactory;

    private EventLoopGroup boss;
    private EventLoopGroup work;
    private Channel channel;

    public GatewaySocketServer(Configuration configuration, DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.configuration = configuration;
        this.gatewaySessionFactory = gatewaySessionFactory;
        initEventLoopGroup();
    }

    private void initEventLoopGroup(){
        boss = new NioEventLoopGroup(configuration.getBossNThreads());
        work = new NioEventLoopGroup(configuration.getWorkNThreads());
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new GatewayChannelInitializer(configuration, gatewaySessionFactory));
            channelFuture = b.bind(configuration.getPort()).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error", e);
        } finally {
            if (channelFuture != null && channelFuture.isSuccess()) {
                logger.info("socket server start done");
            } else {
                logger.info("socket server start fail");
            }
        }
        return channel;
    }
}
