package com.yyl.gateshield.session;

import com.yyl.gateshield.session.defaults.GenericReferenceSessionFactory;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * 该类是一个构建者模式的实现，用于构建 IGenericReferenceSessionFactory 实例，并且返回一个 Future<Channel> 对象，该对象可以用于异步地创建或打开一个会话。
 *
 * 在 build 方法中：
 *
 * 创建了一个 GenericReferenceSessionFactory 的实例，传入了一个 Configuration 对象。这个实例化会进行一些初始化工作或者配置。
 * 调用 openSession() 方法，试图打开一个会话。这个方法可能是一个异步操作，所以它可能会返回一个 Future<Channel> 对象，用于异步地获取 Channel。
 * 在尝试打开会话的过程中，捕获了 ExecutionException 和 InterruptedException 异常，并将其封装成 RuntimeException 抛出。
 * 最终返回了 Future<Channel> 对象，可能在异步操作完成后提供对会话的访问。
 * 总体而言，这个类的目的是封装了 IGenericReferenceSessionFactory 的创建过程，并提供了一个简便的方法来异步地获取到一个会话的 Channel 对象。
 */
public class GenericReferenceSessionFactoryBuilder {

    public Future<Channel> build(Configuration configuration){
        IGenericReferenceSessionFactory genericReferenceSessionFactory = new GenericReferenceSessionFactory(configuration);
        try {
            return genericReferenceSessionFactory.openSession();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
