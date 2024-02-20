package com.yyl.gateshield.test;

import com.yyl.gateshield.core.mapping.HttpCommandType;
import com.yyl.gateshield.core.mapping.HttpStatement;
import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.session.defaults.DefaultGatewaySessionFactory;
import com.yyl.gateshield.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    /**
     * 测试：
     * http://localhost:7397/wg/activity/sayHi
     * 参数：
     * {
     *     "str": "10001"
     * }
     *
     * http://localhost:7397/wg/activity/index
     * 参数：
     * {
     *     "name":"Yulong",
     *     "uid":"10001"
     * }
     */
    public void test_gateway() throws ExecutionException, InterruptedException {
        //1.创建配置信息加载注册
        Configuration configuration = new Configuration();
        configuration.setHostName("127.0.0.1");
        configuration.setPort(7397);

        //2.基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        //3.创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (channel == null) {
            throw new RuntimeException("netty server start error : channel is null");
        }

        while (!channel.isActive()){
            logger.info("netty server gateway starting ...");
            Thread.sleep(500);
        }

        logger.info("netty server gateway start Done! {}", channel.localAddress());

        //注册接口
        configuration.registryConfig("api-gateshield-test", "zookeeper://127.0.0.1:2181", "com.yyl.gateshield.rpc.IActivityBooth", "1.0.0");

        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateshield-test",
                "com.yyl.gateshield.rpc.IActivityBooth",
                "sayHi",
                "java.lang.String",
                "/wg/activity/sayHi",
                HttpCommandType.GET,
                false
        );

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateshield-test",
                "com.yyl.gateshield.rpc.IActivityBooth",
                "insert",
                "com.yyl.gateshield.rpc.dto.XReq",
                "/wg/activity/insert",
                HttpCommandType.POST,
                true
        );

        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);

        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void test(){
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        System.out.println(reference);
    }
}
