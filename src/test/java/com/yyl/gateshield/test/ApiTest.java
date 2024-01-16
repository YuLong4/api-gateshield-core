package com.yyl.gateshield.test;

import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GenericReferenceSessionFactoryBuilder;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    /*
        测试 http://localhost:7397/sayHi
     */
    public void test_GenericReference() throws ExecutionException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.addGenericReference("api-gateshield-test", "com.yyl.gateshield.rpc.IActivityBooth", "sayHi");

        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
        Future<Channel> future = builder.build(configuration);

        logger.info("服务启动完成" + future.get().id());

        Thread.sleep(Long.MAX_VALUE);
    }
}
