package com.yyl.gateshield.test;

import com.yyl.gateshield.mapping.HttpCommandType;
import com.yyl.gateshield.mapping.HttpStatement;
import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.defaults.DefaultGatewaySessionFactory;
import com.yyl.gateshield.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    /*
        测试 http://localhost:7397/wg/activity/sayHi
     */
    public void test_gateway() throws ExecutionException, InterruptedException {
        //1.创建配置信息加载注册
        Configuration configuration = new Configuration();
        HttpStatement httpStatement = new HttpStatement(
                "api-gateshield-test",
                "com.yyl.gateshield.rpc.IActivityBooth",
                "sayHi",
                "/wg/activity/sayHi",
                HttpCommandType.GET
        );
        configuration.addMapper(httpStatement);

        //2.基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        //3.创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (channel == null) {
            throw new RuntimeException("netty server start error : channel is null");
        }

        while(!channel.isActive()){
            logger.info("netty server gateway starting ...");
            Thread.sleep(500);
        }

        logger.info("netty server gateway start Done {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }
}
