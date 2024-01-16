package com.yyl.gateshield.session.defaults;

import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.IGenericReferenceSessionFactory;
import com.yyl.gateshield.session.SessionServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenericReferenceSessionFactory implements IGenericReferenceSessionFactory {

    private final Logger logger = LoggerFactory.getLogger(GenericReferenceSessionFactory.class);

    private final Configuration configuration;

    public GenericReferenceSessionFactory(Configuration configuration) {
        this.configuration = configuration;

    }

    @Override
    public Future<Channel> openSession() throws ExecutionException, InterruptedException {

        SessionServer server = new SessionServer(configuration);

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

        return future;
    }
}
