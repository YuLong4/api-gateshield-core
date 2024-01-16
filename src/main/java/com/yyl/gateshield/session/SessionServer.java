package com.yyl.gateshield.session;

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
 * 定义了一个 SessionServer 类，实现了 Callable<Channel> 接口。它的目的是创建一个服务器，监听特定端口并处理传入的连接请求。
 *
 * 在 call 方法中：
 *
 * 创建了两个 EventLoopGroup 实例，一个用作主线程组 boss，另一个作为工作线程组 work，用于处理连接请求和实际的业务逻辑。
 * 使用 ServerBootstrap 配置了服务器。设置了主线程组、工作线程组、使用的 NioServerSocketChannel 类型的 Channel、设置了 Socket 的一些选项（比如 SO_BACKLOG）以及设置了 SessionChannelInitializer 作为新连接的处理器。
 * 通过 bind 方法绑定了服务器到指定端口（这里是 7397），并使用 syncUninterruptibly 同步等待绑定操作完成。
 * 捕获了可能发生的异常，在 finally 块中根据绑定操作的结果记录日志，标记服务器的启动状态。
 * 最后，SessionServer 类实现了 Callable<Channel> 接口，返回了服务器所绑定的 Channel 对象，这样可以在其他地方获取到服务器的通道对象，进行进一步的操作或者管理。
 *
 * 这个类代表了一个简单的基于 Netty 的服务器实现，它能够接受连接并使用 SessionChannelInitializer 中定义的处理器来处理传入的请求。
 */
public class SessionServer implements Callable<Channel> {

    private final Logger logger = LoggerFactory.getLogger(SessionServer.class);
    private final EventLoopGroup boss = new NioEventLoopGroup(1);
    private final EventLoopGroup work = new NioEventLoopGroup();
    private Channel channel;
    private Configuration configuration;

    public SessionServer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SessionChannelInitializer(configuration));

            channelFuture = b.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error");
        }finally {
            if(channelFuture != null && channelFuture.isSuccess()){
                logger.info("socket server start done");
            } else {
                logger.info("socket server start fail");
            }
        }
        return channel;
    }
}
