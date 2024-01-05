package com.yyl.gateshield.test;

import com.yyl.gateshield.session.SessionServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 在 test 方法中：
 * 创建了一个 SessionServer 实例。
 * 使用 Executors.newFixedThreadPool(2).submit(server) 提交了 SessionServer 实例到一个固定大小为 2 的线程池中，并获得了一个 Future<Channel> 对象。
 * 通过 future.get() 方法获取了 Channel 对象，即服务器所绑定的通道。如果获取到的 Channel 为 null，则抛出异常。
 * 使用一个 while 循环，检查通道是否处于激活状态。在这个例子中，通过不断地检查直到通道激活为止。
 * 一旦通道激活，输出日志表明 Netty 服务器已经启动完成，同时打印了服务器的本地地址。
 * 最后调用了 Thread.sleep(Long.MAX_VALUE) 来保持测试程序的运行，使得服务器保持运行状态。
 * 这个测试类主要目的是启动 SessionServer，等待它成功启动并且输出相应的日志信息，以确保服务器的正常运行。通常情况下，测试类可以用来验证服务器的初始化和启动过程是否正确，以及在启动后是否能够处理来自客户端的请求。
 */
public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        SessionServer server = new SessionServer();

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (channel == null) {
            throw new RuntimeException("netty server start error, channel is null");
        }

        while(!channel.isActive()){
            logger.info("netty启动服务中...");
            Thread.sleep(500);
        }

        logger.info("netty服务启动完成 ip:端口 {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }
}
